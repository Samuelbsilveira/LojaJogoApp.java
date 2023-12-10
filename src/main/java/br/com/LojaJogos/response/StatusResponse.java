package br.com.LojaJogos.response;

public enum StatusResponse {
    SUCCESS ("Success"),
    ERROR ("Error");
 
    private String status;

    StatusResponse(String success) {
    }
    // constructors, getters
}