package com.basoft.eorder.domain.model;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class TnuPayReqResult extends TnuResult {
    private String cust_trade_no;

    public String getCust_trade_no() {
        return cust_trade_no;
    }

    public void setCust_trade_no(String cust_trade_no) {
        this.cust_trade_no = cust_trade_no;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public final static class Builder {
        private String cust_trade_no;
        private String return_code;
        private String return_msg;

        public Builder cust_trade_no(String cust_trade_no) {
            this.cust_trade_no = cust_trade_no;
            return this;
        }

        public Builder return_code(String return_code) {
            this.return_code = return_code;
            return this;
        }

        public Builder return_msg(String return_msg) {
            this.return_msg = return_msg;
            return this;
        }

        public TnuPayReqResult build() {
            TnuPayReqResult result = new TnuPayReqResult();
            result.cust_trade_no = this.cust_trade_no;
            result.setReturn_code(this.return_code);
            result.setReturn_msg(this.return_msg);
            return result;
        }
    }
}
