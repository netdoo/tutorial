package com.exxodusdb.domain;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class EPFile extends ArrayList<EPFileLine> {
    boolean isAllEP;

    public EPFile(boolean isAllEP) {
        this.isAllEP = isAllEP;
    }

    public void setIsAllEP(boolean isAllEP) {
        this.isAllEP = isAllEP;
    }

    public boolean getIsAllEP() {
        return this.isAllEP;
    }

    public static String getEPHeader(boolean isAllEP) {
        if (isAllEP) {
            return "id\ttitle\tprice_pc\tprice_mobile\tnormal_price\tlink\tmobile_link\timage_link\tcategory_name1\tcategory_name2\tcategory_name3\tcategory_name4\tnaver_category\tnaver_product_id\tcondition\timport_flag\tparallel_import\torder_made\tproduct_flag\tadult\tmodel_number\tbrand\tmaker\tevent_words\tpartner_coupon_download\tinstallation_costs\tpre_match_code\tsearch_tag\tgroup_id\tcoordi_id\tminimum_purchase_quantity\treview_count\tshipping\tdelivery_grade\tdelivery_detail";
        }

        return "id\ttitle\tprice_pc\tprice_mobile\tnormal_price\tlink\tmobile_link\timage_link\tcategory_name1\tcategory_name2\tcategory_name3\tcategory_name4\tnaver_category\tnaver_product_id\tcondition\timport_flag\tparallel_import\torder_made\tproduct_flag\tadult\tmodel_number\tbrand\tmaker\tevent_words\tpartner_coupon_download\tinstallation_costs\tpre_match_code\tsearch_tag\tgroup_id\tcoordi_id\tminimum_purchase_quantity\treview_count\tshipping\tdelivery_grade\tdelivery_detail\tclass\tupdate_time";
    }

    public void save(String path) throws Exception {

        File saveFile = new File(path);

        if (saveFile.exists()) {
            saveFile.delete();
        }

        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(saveFile.toPath(), StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW))) {

            out.println(getEPHeader(isAllEP));

            this.stream().forEach(line -> {
                out.println(line);
            });
        }
    }
}
