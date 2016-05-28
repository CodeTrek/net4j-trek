
/*
WARNING: THIS FILE IS AUTO-GENERATED. DO NOT MODIFY.

This file was generated from .idl using "rtiddsgen".
The rtiddsgen tool is part of the RTI Connext distribution.
For more information, type 'rtiddsgen -help' at a command shell
or consult the RTI Connext manual.
*/

package org.code.trek.net4j.r2.dds;

import com.rti.dds.typecode.ExtensibilityKind;
import com.rti.dds.typecode.StructMember;
import com.rti.dds.typecode.TCKind;
import com.rti.dds.typecode.TypeCode;
import com.rti.dds.typecode.TypeCodeFactory;

public class RequestReplyTypeCode {
    public static final TypeCode VALUE = getTypeCode();

    private static TypeCode getTypeCode() {
        TypeCode tc = null;
        int __i = 0;
        StructMember sm[] = new StructMember[2];

        sm[__i] = new StructMember("clientId", false, (short) -1, true, (TypeCode) new TypeCode(TCKind.TK_STRING, 255),
                0, false);
        __i++;
        sm[__i] = new StructMember("payload", false, (short) -1, false, (TypeCode) new TypeCode(256, TypeCode.TC_OCTET),
                1, false);
        __i++;

        tc = TypeCodeFactory.TheTypeCodeFactory.create_struct_tc("RequestReply",
                ExtensibilityKind.EXTENSIBLE_EXTENSIBILITY, sm);
        return tc;
    }
}
