package com.exasyncresttemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

/**
    @note   How to create HttpEntity

     MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
     params.set("message", "test post" + Integer.toString(i));
     HttpHeaders headers = new HttpHeaders();
     headers.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
     headers.add("X-Authorization", "API키값");
     HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

    asyncHttp.addRequest(new AsyncBulkHttpRequestBuilder("http://localhost:8080/postecho", HttpMethod.POST, httpEntity));
 */
public class AsyncBulkHttpRequestBuilder {

    String url;
    HttpMethod method;
    HttpEntity<?> requestEntity;

    public AsyncBulkHttpRequestBuilder(String url, HttpMethod method) {
        this.url = url;
        this.method = method;
        this.requestEntity = null;
    }

    public AsyncBulkHttpRequestBuilder(String url, HttpMethod method, HttpEntity<?> requestEntity) {
        this.url = url;
        this.method = method;
        this.requestEntity = requestEntity;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return this.url;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }
    public HttpMethod getMethod() {
        return this.method;
    }

    public void setRequestEntity(HttpEntity<?> requestEntity) {
        this.requestEntity = requestEntity;
    }
    public HttpEntity<?> getRequestEntity() {        return this.requestEntity;    }

    @Override
    public int hashCode() {
        return 31 * this.url.hashCode();    /// 31 is prime number
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        AsyncBulkHttpRequestBuilder other = (AsyncBulkHttpRequestBuilder) obj;

        return this.method == other.method &&
                this.requestEntity.equals(other.requestEntity) &&
                this.url.equals(other.url);
    }
}
