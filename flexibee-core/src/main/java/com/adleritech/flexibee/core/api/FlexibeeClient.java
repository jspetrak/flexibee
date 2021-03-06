package com.adleritech.flexibee.core.api;

import com.adleritech.flexibee.core.api.domain.AddressBookResponse;
import com.adleritech.flexibee.core.api.domain.InternalDocumentResponse;
import com.adleritech.flexibee.core.api.domain.IssuedInvoiceResponse;
import com.adleritech.flexibee.core.api.domain.WinstromRequest;
import com.adleritech.flexibee.core.api.domain.WinstromResponse;
import lombok.Getter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.io.IOException;

public class FlexibeeClient {

    private static final String API_BASE_URL = "https://demo.flexibee.eu:5434";

    private final String company;

    @Getter
    private final Api client;

    public FlexibeeClient(String username, String password, String company) {
        this.company = company;
        client = RetrofitClientFactory.createService(Api.class, API_BASE_URL, username, password);
    }

    public FlexibeeClient(String username, String password, String company, String apiBaseUrl) {
        this.company = company;
        client = RetrofitClientFactory.createService(Api.class, apiBaseUrl, username, password);
    }

    public WinstromResponse createInvoice(WinstromRequest winstromRequest) throws IOException, FlexibeeException {
        Response<WinstromResponse> response = client.issueInvoice(company, winstromRequest).execute();
        handleErrorResponse(response);
        return response.body();
    }

    public WinstromResponse createInternalDocument(WinstromRequest winstromRequest) throws IOException, FlexibeeException {
        Response<WinstromResponse> response = client.createInternalDocument(company, winstromRequest).execute();
        handleErrorResponse(response);
        return response.body();
    }

    public WinstromResponse createAddressBook(WinstromRequest winstromRequest) throws IOException, FlexibeeException {
        Response<WinstromResponse> response = client.createAddressBook(company, winstromRequest).execute();
        handleErrorResponse(response);
        return response.body();
    }

    public WinstromResponse createOrder(WinstromRequest winstromRequest) throws IOException, FlexibeeException {
        Response<WinstromResponse> response = client.createOrder(company, winstromRequest).execute();
        handleErrorResponse(response);
        return response.body();
    }

    private void handleErrorResponse(Response response) throws FlexibeeException {
        if (response.code() == 404) {
            throw new NotFound();
        }
        if (!response.isSuccessful()) {
            throw new FlexibeeException("Flexibee error" +
                " httpStatusCode=" + response.code() +
                " errorBody=" + getErrorBody(response)
            );
        }
    }

    private String getErrorBody(Response response) {
        try {
            return response.errorBody().string();
        } catch (IOException e) {
            return "--Can not read error body--";
        }
    }

    public AddressBookResponse findAddressBookByRegNo(String regNo) throws IOException, FlexibeeException {
        Response<AddressBookResponse> response = client.findAddressBookByRegNo(company, regNo).execute();
        handleErrorResponse(response);
        return response.body();
    }

    public AddressBookResponse findAddressBookfindAddressBookByExternalId(String externalId) throws IOException, FlexibeeException {
        Response<AddressBookResponse> response = client.findAddressBookByExternalId(company, externalId).execute();
        handleErrorResponse(response);
        return response.body();
    }

    public ResponseBody downloadIssuedInvoiceAsPdf(String id) throws IOException, FlexibeeException {
        Response<ResponseBody> response = client.downloadIssuedInvoiceAsPdf(company, id).execute();
        handleErrorResponse(response);
        return response.body();
    }

    public IssuedInvoiceResponse getIssuedInvoice(String id) throws IOException, FlexibeeException {
        Response<IssuedInvoiceResponse> response = client.getIssuedInvoice(company, id).execute();
        handleErrorResponse(response);
        return response.body();
    }

    public InternalDocumentResponse getInternalDocument(String id) throws IOException, FlexibeeException {
        Response<InternalDocumentResponse> response = client.getInternalDocument(company, id).execute();
        handleErrorResponse(response);
        return response.body();
    }

    public AddressBookResponse findAddressBookByCode(String code) throws IOException, FlexibeeException {
        Response<AddressBookResponse> response = client.findAddressBookByCode(company, code).execute();
        handleErrorResponse(response);
        return response.body();
    }

    public WinstromResponse updateAddressBook(String id, WinstromRequest request) throws IOException, FlexibeeException {
        Response<WinstromResponse> response = client.updateAddressBook(company, id, request).execute();
        if (!response.isSuccessful()) {
            String errorBody = getErrorBody(response);
            throw new FlexibeeException("Address book update failed for " +
                    "company=" + id + ", " +
                    "httpStatusCode=" + response.code() + "," +
                    "responseBody=" + errorBody + "," +
                    "requestBody=" + request.toString());
        }
        return response.body();
    }

    public AddressBookResponse searchInAddressBook(String q) throws IOException, FlexibeeException {
        Response<AddressBookResponse> response = client.searchInAddressBook(company, q).execute();
        handleErrorResponse(response);
        return response.body();
    }

    interface Api {

        @PUT("/c/{company}/faktura-vydana.xml")
        Call<WinstromResponse> issueInvoice(@Path("company") String company, @Body WinstromRequest request);

        @GET("/c/{company}/faktura-vydana/{id}.pdf")
        Call<ResponseBody> downloadIssuedInvoiceAsPdf(@Path("company") String company, @Path("id") String id);

        @GET("/c/{company}/faktura-vydana/{id}.xml")
        Call<IssuedInvoiceResponse> getIssuedInvoice(@Path("company") String company, @Path("id") String id);

        @PUT("/c/{company}/interni-doklad.xml")
        Call<WinstromResponse> createInternalDocument(@Path("company") String company, @Body WinstromRequest request);

        @GET("/c/{company}/interni-doklad/{id}.xml")
        Call<InternalDocumentResponse> getInternalDocument(@Path("company") String company, @Path("id") String id);

        @GET("c/{company}/adresar/in:{regNo}.xml")
        Call<AddressBookResponse> findAddressBookByRegNo(@Path("company") String company, @Path("regNo") String regNo);

        @GET("c/{company}/adresar/ext:{regNo}.xml")
        Call<AddressBookResponse> findAddressBookByExternalId(@Path("company") String company, @Path("regNo") String externalId);

        @GET("c/{company}/adresar/(kod='{kod}').xml")
        Call<AddressBookResponse> findAddressBookByCode(@Path("company") String company, @Path("kod") String kod);

        @GET("/c/{company}/adresar.xml")
        Call<AddressBookResponse> searchInAddressBook(@Path("company") String company, @Query("q") String q);

        @PUT("/c/{company}/adresar/{id}.xml")
        Call<WinstromResponse> updateAddressBook(@Path("company") String company, @Path("id") String id, @Body WinstromRequest request);

        @PUT("/c/{company}/adresar.xml")
        Call<WinstromResponse> createAddressBook(@Path("company") String company, @Body WinstromRequest request);

        @PUT("/c/{company}/zakazka.xml")
        Call<WinstromResponse> createOrder(@Path("company") String company, @Body WinstromRequest request);

    }

    public static class NotFound extends FlexibeeException {
        public NotFound() {
            super("");
        }
    }

    public static class FlexibeeException extends Exception {
        private FlexibeeException(String s) {
            super(s);
        }
    }

}
