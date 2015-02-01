/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.jvmx;

import org.eclipse.net4j.acceptor.IAcceptor;

/**
 * An {@link IAcceptor acceptor} that implements JVM-embedded transport.
 * 
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IJvmxAcceptor extends IAcceptor {
    public String getName();
}
