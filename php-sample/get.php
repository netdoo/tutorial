<?php
    echo "test get branch_no : " . $_GET['branch_no'];

    $headers = apache_request_headers();

    echo "<br/>";

    foreach ($headers as $header => $value) {
        echo "$header: $value <br />\n";
    }
?>
