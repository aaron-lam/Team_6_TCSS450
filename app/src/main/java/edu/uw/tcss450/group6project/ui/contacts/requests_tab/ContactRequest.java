package edu.uw.tcss450.group6project.ui.contacts.requests_tab;

import edu.uw.tcss450.group6project.ui.contacts.Contact;

public class ContactRequest {

    private String mMemberId;
    private String mUsername;

    public ContactRequest(final Builder builder) {
        mMemberId = builder.mMemberId;
        mUsername = builder.mUsername;
    }

    public String getMemberId() {
        return mMemberId;
    }

    public String getUsername() {
        return mUsername;
    }

    public static class Builder {
        private final String mMemberId;
        private final String mUsername;


        public Builder(String username, String memberId) {
            this.mUsername = username;
            this.mMemberId = memberId;
        }

        public ContactRequest build() {
            return new ContactRequest(this);
        }
    }
}
