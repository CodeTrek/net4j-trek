/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.tests;

import junit.framework.TestCase;

import org.eclipse.net4j.util.event.IEvent;
import org.eclipse.net4j.util.event.IListener;
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
            // @formatter:off
            System.out.println(
                    "Executing transition: " + getClass().getSimpleName() + 
                    " light switch: " + subject.getState());
            // @formatter:on
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

            init(LightSwitchState.OFF, LightSwitchEvent.TURN_ON, LightSwitchState.ON);
            init(LightSwitchState.OFF, LightSwitchEvent.TURN_OFF, FAIL);

            init(LightSwitchState.ON, LightSwitchEvent.TURN_OFF, LightSwitchState.OFF);
            init(LightSwitchState.ON, LightSwitchEvent.TURN_ON, FAIL);
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

    public void testOffOnOffTransition() {
        LightFsm fsm = new LightFsm();
        Light light = new Light();
        fsm.process(light, LightSwitchEvent.INIT, fsm);
        assertEquals(LightSwitchState.OFF, light.getState());

        fsm.addListener(new IListener() {
            @Override
            public void notifyEvent(IEvent event) {
                @SuppressWarnings("unchecked")
                FiniteStateMachine<LightSwitchState, LightSwitchEvent, Light>.StateChangedEvent e = (FiniteStateMachine<LightSwitchState, LightSwitchEvent, Light>.StateChangedEvent) event;
                System.out.println("state chanage: old state: " + e.getOldState() + " new state: " + e.getNewState());
            }
        });

        fsm.process(light, LightSwitchEvent.TURN_ON, null);
        assertEquals(LightSwitchState.ON, light.getState());

        fsm.process(light, LightSwitchEvent.TURN_OFF, null);
        assertEquals(LightSwitchState.OFF, light.getState());
    }

    public void testIllegalStateException() {
        LightFsm fsm = new LightFsm();
        Light light = new Light();

        try {
            fsm.process(light, LightSwitchEvent.TURN_ON, null);
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        fsm.process(light, LightSwitchEvent.INIT, fsm);

        try {
            fsm.process(light, LightSwitchEvent.TURN_OFF, null);
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        fsm.process(light, LightSwitchEvent.TURN_ON, fsm);

        try {
            fsm.process(light, LightSwitchEvent.TURN_ON, null);
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}