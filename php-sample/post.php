<?php
    if (empty($_POST['branch_no'])) {
        echo "test post branch no is empty";
    } else {
        echo "test post branch_no : " . $_POST['branch_no'];
    }

    echo "<br/>";

    $headers = apache_request_headers();

    foreach ($headers as $header => $value) {
        echo "$header: $value <br />\n";
    }
?>

