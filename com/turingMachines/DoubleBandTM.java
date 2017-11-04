package com.turingMachines;

import java.io.File;
import java.util.*;

public class DoubleBandTM {
    private Set<String> StateSpace;
    //private Set<Transition> TransitionSpace;
    private HashMap<String, HashMap> TransitionMap;
    private Set<String> FinalStateSpace;
    private String StartState;

    private String Tape1;
    private String Tape2;
    private String CurrentState;
    private int tapeIndex1;
    private int tapeIndex2;

    class Transition {
        String currentState;
        char tape1SymbolRead;
        char tape2SymbolRead;
        String nextState;
        char tape1SymbolWrite;
        char tape2SymbolWrite;
        char tape1MoveDirection;
        char tape2MoveDirection;

        public Transition() {
        }
        @Override
        public String toString() {
            return "Transition{" +
                    "currentState='" + currentState + '\'' +
                    ", tape1SymbolRead=" + tape1SymbolRead +
                    ", tape2SymbolRead=" + tape2SymbolRead +
                    ", nextState='" + nextState + '\'' +
                    ", tape1SymbolWrite=" + tape1SymbolWrite +
                    ", tape2SymbolWrite=" + tape2SymbolWrite +
                    ", tape1MoveDirection=" + tape1MoveDirection +
                    ", tape2MoveDirection=" + tape2MoveDirection +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DoubleBandTM{" +
                "StartState='" + StartState + '\'' +
                ", Tape1='" + Tape1 + '\'' +
                ", Tape2='" + Tape2 + '\'' +
                ", CurrentState='" + CurrentState + '\'' +
                ", tapeIndex1=" + tapeIndex1 +
                ", tapeIndex2=" + tapeIndex2 +
                '}';
    }
    public DoubleBandTM(String fileName){
        StateSpace = new HashSet<String>();
        //TransitionSpace = new HashSet<Transition>();
        TransitionMap = new HashMap<String, HashMap>();
        FinalStateSpace = new HashSet<String>();
        StartState = "";
        Tape1 = "";
        Tape2 = "";
        CurrentState = "";
        tapeIndex1 = 0;
        tapeIndex2 = 0;
        try {

            File file = new File(fileName);

            Scanner input = new Scanner(file);

            int nrStates=input.nextInt();
            while(nrStates>0){
                String state=input.next();
                StateSpace.add(state);
                nrStates--;
            }
            int nrFinalStates = input.nextInt();
            while(nrFinalStates>0){
                String state=input.next();
                System.out.println(state);
                FinalStateSpace.add(state);
                nrFinalStates--;
            }
            String initialState=input.next();
            setStartState(initialState);
            int nrTransitions=input.nextInt();
            while(nrTransitions>0){
                Transition transition= new Transition();
                transition.currentState=input.next();
                transition.tape1SymbolRead=input.next().charAt(0);
                transition.tape2SymbolRead=input.next().charAt(0);
                transition.nextState=input.next();
                transition.tape1SymbolWrite=input.next().charAt(0);
                transition.tape1MoveDirection=input.next().charAt(0);
                transition.tape2SymbolWrite=input.next().charAt(0);
                transition.tape2MoveDirection=input.next().charAt(0);
                if(TransitionMap.get(transition.currentState)==null){
                    TransitionMap.put(transition.currentState,new HashMap<String,HashMap>());
                }
                if(TransitionMap.get(transition.currentState).get(transition.tape1SymbolRead+""
                        +transition.tape2SymbolRead)==null){
                    HashMap hm1=TransitionMap.get(transition.currentState);
                    hm1.put(transition.tape1SymbolRead+""
                            +transition.tape2SymbolRead,new HashMap<String,Transition>());
                }

                TransitionMap.get(transition.currentState).put(transition.tape1SymbolRead+""
                        +transition.tape2SymbolRead,transition);
                nrTransitions--;
            }
            Tape1=input.nextLine();//nextLine="" otherwise
            Tape1=input.nextLine();
            Tape2=input.nextLine();
            input.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getTape1() {
        return Tape1;
    }

    public String getTape2() {
        return Tape2;
    }

    public boolean Run(boolean silentmode) {
        CurrentState = StartState;
        tapeIndex1++;
        tapeIndex2++;
        while(!FinalStateSpace.contains(CurrentState)) {
            boolean foundTransition = false;
            Transition CurrentTransition = null;
            if (!silentmode) {
                if (tapeIndex1 >0 && tapeIndex2 >0) {
                    System.out.println(Tape1.substring(0, tapeIndex1) + " " + CurrentState + " " + Tape1.substring(tapeIndex1));
                    System.out.println(Tape2.substring(0, tapeIndex2) + " " + CurrentState + " " + Tape2.substring(tapeIndex2));
                }
                else {
                    System.out.println(" " + CurrentState + " " + Tape1.substring(tapeIndex1));
                    System.out.println(" " + CurrentState + " " + Tape2.substring(tapeIndex2));
                }
            }

            CurrentTransition=(Transition) TransitionMap.get(CurrentState).get(Tape1.charAt(tapeIndex1)+""+Tape2.charAt(tapeIndex2));
            if(CurrentTransition==null)foundTransition = false;
            else foundTransition=true;

            if (!foundTransition) {
                System.err.println ("The machine has bocked! (state="
                        + CurrentState + ", symbol1=" + Tape1.charAt(tapeIndex1)
                        + ", symbol2=" + Tape2.charAt(tapeIndex2) + ")");
                return false;
            } else {
                CurrentState = CurrentTransition.nextState;
                char[] tempTape1 = Tape1.toCharArray();
                char[] tempTape2 = Tape2.toCharArray();
                tempTape1[tapeIndex1] = CurrentTransition.tape1SymbolWrite;
                tempTape2[tapeIndex2] = CurrentTransition.tape2SymbolWrite;
                Tape1 =  new String(tempTape1);
                Tape2 =  new String(tempTape2);
                if(CurrentTransition.tape1MoveDirection=='R') tapeIndex1++;
                else if(CurrentTransition.tape1MoveDirection=='L')tapeIndex1--;
                if (tapeIndex1 < 0)
                    tapeIndex1 = 0;
                if(CurrentTransition.tape2MoveDirection=='R') tapeIndex2++;
                else if(CurrentTransition.tape2MoveDirection=='L')tapeIndex2--;
                if (tapeIndex2 < 0)
                    tapeIndex2 = 0;

                while (Tape1.length() <= tapeIndex1) {
                    Tape1 = Tape1.concat("#");
                }
                while (Tape2.length() <= tapeIndex2) {
                    Tape2 = Tape2.concat("#");
                }


            }


        }

        return FinalStateSpace.contains(CurrentState);


    }
    private void setStartState(String newStartState) {
            StartState = newStartState;
    }
}