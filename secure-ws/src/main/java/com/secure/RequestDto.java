package com.secure;

/**
 * Created by pradeep on 21/7/17.
 */
public class RequestDto {

    private Vendor vendor;
    private String data;

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RequestDto{" +
                "vendor=" + vendor +
                ", data='" + data + '\'' +
                '}';
    }
}
