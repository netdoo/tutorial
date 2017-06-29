<?php
/**
	http://stackoverflow.com/questions/9932636/how-to-set-hostname-using-php-curl-for-a-specific-ip
*/
define ('SERVICE_API_HOST', "foo.localhost.net:4313,bar.localhost.net:4313,zoo.localhost.net:4313");

function execute_service_api_post($api_url, $query_string) {

    $contents = "";
    $host_list = explode(',', SERVICE_API_HOST);

    foreach ($host_list as $host) {

        $ch = curl_init();

        curl_setopt($ch, CURLOPT_URL, $host.$api_url.$query_string);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Host: service.localhost.net'));
        curl_setopt($ch, CURLOPT_RESOLVE, ["bar.localhost.net:4313:127.0.0.1"]);
        curl_setopt($ch, CURLOPT_POST, TRUE);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 3); 
        curl_setopt($ch, CURLOPT_TIMEOUT, 3); //timeout in seconds
        
        /// host가 http인 경우에만 추가함.
        curl_setopt($ch, CURLOPT_FAILONERROR, true);
        
        /// host가 https인 경우에만 추가함.
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);
    
        $contents = curl_exec($ch);

        if (curl_error($ch))
        {
            echo 'error : ' . curl_error($ch) . "<br/>";
            curl_close($ch);
            continue;
        }

        curl_close($ch);
        break;
    }

    return $contents;
}

function execute_service_api_get($api_url, $query_string) {

    $contents = "";
    $host_list = explode(',', SERVICE_API_HOST);

    foreach ($host_list as $host) {
        $ch = curl_init();

        curl_setopt($ch, CURLOPT_URL, $host.$api_url.$query_string);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Host: service.localhost.net'));
        curl_setopt($ch, CURLOPT_RESOLVE, ["bar.localhost.net:4313:127.0.0.1"]);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 3); 
        curl_setopt($ch, CURLOPT_TIMEOUT, 3); //timeout in seconds
        
        /// host가 http인 경우에만 추가함.
        curl_setopt($ch, CURLOPT_FAILONERROR, true);
        
        /// host가 https인 경우에만 추가함.
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);

        $contents = curl_exec($ch);

        if (curl_error($ch))
        {
            echo 'error : ' . curl_error($ch) . "<br/>";
            curl_close($ch);
            continue;
        }

        curl_close($ch);
        break;
    }

    return $contents;
}

$start = time();
$ret = execute_service_api_get('/get.php', '?branch_no=1');
echo 'get >> ' . $ret;
echo '<br/>';

$ret = execute_service_api_post('/post.php', '?branch_no=1');
echo 'post >> ' . $ret;

echo '<br/>';
echo 'elapsed : ' . (time() - $start);
?>
