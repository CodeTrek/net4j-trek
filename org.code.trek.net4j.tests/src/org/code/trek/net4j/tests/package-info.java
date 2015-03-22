/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */

/**
 * I'm starting a coding exercise to become familiar with the net4j communications framework in general and to learn how
 * to develop a custom net4j transport in particular.
 * <p>
 * 
 * I'll start by implementing my own version of a JVM protocol similar to the one that ships with netj4.
 * <p>
 * 
 * But first, since I'm extending existing net4j frameworks, I want to explore fundamental net4j framework concepts. I'm
 * using unit tests to do this. The idea is to use the JUnit testing framework as a way to create a collection of
 * executable code samples. So the point is not to "test" net4j for correct behavior, but to demonstrate  net4j's
 * behavior in small, easy to digest chunks that just happen to be in the form of unit tests.
 * <p>
 *
 * <ul>
 * <li>{@link org.code.trek.net4j.tests.OmPlatformTest OM Platform Unit Tests}</li>
 * <li>{@link org.code.trek.net4j.tests.OmBundleTest OM Bundle Unit Tests}</li>
 * <li>{@link org.code.trek.net4j.tests.BufferTest Buffer Tests}</li>
 * </ul>
 * 
 */
package org.code.trek.net4j.tests;