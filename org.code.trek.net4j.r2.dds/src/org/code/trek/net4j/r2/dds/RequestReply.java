
/*
WARNING: THIS FILE IS AUTO-GENERATED. DO NOT MODIFY.

This file was generated from .idl using "rtiddsgen".
The rtiddsgen tool is part of the RTI Connext distribution.
For more information, type 'rtiddsgen -help' at a command shell
or consult the RTI Connext manual.
*/

package org.code.trek.net4j.r2.dds;

import java.io.Serializable;

import com.rti.dds.cdr.CdrHelper;
import com.rti.dds.infrastructure.ByteSeq;
import com.rti.dds.infrastructure.Copyable;

public class RequestReply implements Copyable, Serializable {

    public String clientId = ""; /* maximum length = (255) */
    public ByteSeq payload = new ByteSeq((256));

    public RequestReply() {

    }

    public RequestReply(RequestReply other) {

        this();
        copy_from(other);
    }

    public static Object create() {

        RequestReply self;
        self = new RequestReply();
        self.clear();
        return self;

    }

    public void clear() {

        clientId = "";
        if (payload != null) {
            payload.clear();
        }
    }

    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        RequestReply otherObj = (RequestReply) o;

        if (!clientId.equals(otherObj.clientId)) {
            return false;
        }
        if (!payload.equals(otherObj.payload)) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int __result = 0;
        __result += clientId.hashCode();
        __result += payload.hashCode();
        return __result;
    }

    /**
     * This is the implementation of the <code>Copyable</code> interface. This method will perform a deep copy of
     * <code>src</code> This method could be placed into <code>RequestReplyTypeSupport</code> rather than here by using
     * the <code>-noCopyable</code> option to rtiddsgen.
     * 
     * @param src
     *            The Object which contains the data to be copied.
     * @return Returns <code>this</code>.
     * @exception NullPointerException
     *                If <code>src</code> is null.
     * @exception ClassCastException
     *                If <code>src</code> is not the same type as <code>this</code>.
     * @see com.rti.dds.infrastructure.Copyable#copy_from(java.lang.Object)
     */
    public Object copy_from(Object src) {

        RequestReply typedSrc = (RequestReply) src;
        RequestReply typedDst = this;

        typedDst.clientId = typedSrc.clientId;
        typedDst.payload.copy_from(typedSrc.payload);

        return this;
    }

    public String toString() {
        return toString("", 0);
    }

    public String toString(String desc, int indent) {
        StringBuffer strBuffer = new StringBuffer();

        if (desc != null) {
            CdrHelper.printIndent(strBuffer, indent);
            strBuffer.append(desc).append(":\n");
        }

        CdrHelper.printIndent(strBuffer, indent + 1);
        strBuffer.append("clientId: ").append(clientId).append("\n");
        CdrHelper.printIndent(strBuffer, indent + 1);
        strBuffer.append("payload: ");
        for (int i__ = 0; i__ < payload.size(); ++i__) {
            if (i__ != 0)
                strBuffer.append(", ");
            strBuffer.append(payload.get(i__));
        }
        strBuffer.append("\n");

        return strBuffer.toString();
    }

}
