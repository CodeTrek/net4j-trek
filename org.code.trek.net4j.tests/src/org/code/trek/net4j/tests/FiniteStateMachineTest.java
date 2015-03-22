/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.tests;

import junit.framework.TestCase;

import org.eclipse.net4j.util.fsm.FiniteStateMachine;
import org.eclipse.net4j.util.fsm.ITransition;

/**
 * These tests explore the net4j <code>FiniteStateMachine</code> concept.
 * <p>
 */
public class FiniteStateMachineTest extends TestCase {

    enum LightSwitchState {
        UNKNOWN, ON, OFF
    }

    enum LightSwitchEvent {
        INIT, TURN_ON, TURN_OFF
    }

    class Light {
        LightSwitchState state = LightSwitchState.UNKNOWN;

        void setState(LightSwitchState value) {
            state = value;
        }

        LightSwitchState getState() {
            return state;
        }
    }

    class InitLightTransition implements ITransition<LightSwitchState, LightSwitchEvent, Light, LightFsm> {
        @Override
        public void execute(Light subject, LightSwitchState state, LightSwitchEvent event, LightFsm fsm) {
            System.out.println("Executing transition: " + getClass().getSimpleName() + " light switch: "
                    + subject.getState());
            fsm.setState(subject, LightSwitchState.OFF);
        }
    }

    class LightFsm extends FiniteStateMachine<LightSwitchState, LightSwitchEvent, Light> {

        @SuppressWarnings("unchecked")
        LightFsm() {
            super(LightSwitchState.class, LightSwitchEvent.class);
            init(LightSwitchState.UNKNOWN, LightSwitchEvent.INIT, new InitLightTransition());
            init(LightSwitchState.UNKNOWN, LightSwitchEvent.TURN_OFF, FAIL);
            init(LightSwitchState.UNKNOWN, LightSwitchEvent.TURN_ON, FAIL);
        }

        @Override
        protected LightSwitchState getState(Light subject) {
            return subject.getState();
        }

        @Override
        protected void setState(Light subject, LightSwitchState state) {
            subject.setState(state);
        }
    }

    public void testInitLightTransition() {
        LightFsm fsm = new LightFsm();
        Light light = new Light();

        assertEquals(LightSwitchState.UNKNOWN, light.getState());

        fsm.process(light, LightSwitchEvent.INIT, fsm);

        assertEquals(LightSwitchState.OFF, light.getState());
    }
}