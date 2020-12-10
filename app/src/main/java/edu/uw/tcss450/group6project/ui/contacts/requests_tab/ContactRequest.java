package edu.uw.tcss450.group6project.ui.contacts.requests_tab;

/** Used to construct a contact request object, used on the contact request tab of contacts
 * @author chasealder
 */
public class ContactRequest {

    private String mMemberId;
    private String mUsername;

    public ContactRequest(final Builder builder) {
        mMemberId = builder.mMemberId;
        mUsername = builder.mUsername;
    }

    /** MemberId getter
     *
     * @return MemberId of the user who wants to be contacts
     */
    public String getMemberId() {
        return mMemberId;
    }

    /** Username getter
     *
     * @return Username of the user who wants to be contacts
     */
    public String getUsername() {
        return mUsername;
    }

    /** Helper class for creating immutable contact request object
     *
     */
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
