import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StringSizeFactory {

    private final static StringSizeFactory INSTANCE = new StringSizeFactory();
    private final static Logger LOGGER = LoggerFactory.getLogger(StringSizeFactory.class);

    private long overallExecutionCycleAndOperationSystemMillisTimestamp;
    private BufferedReader standardisedBufferedInputReaderReferenceVariable;
    private String stringSizeStringStorageVariable;
    private int stringSizeTemporaryStorageVariable;

    public static void main(String[] args) {
        INSTANCE.initialise();
    }

    private void initialise() {
        LOGGER.info("Initialising StringSizeFactory...");
        LOGGER.info("Initialising timestamp for execution time calculation...");
        this.overallExecutionCycleAndOperationSystemMillisTimestamp = System.currentTimeMillis();
        this.passThroughToConsoleForInput();
        this.requestInputFromUserViaBufferedInputStream();
        this.calculateStringSizeDeterminedByUserInputFromBufferedInputStream();
        this.exit();
    }

    private void passThroughToConsoleForInput() {
        LOGGER.info("Initialising System Millisecond-based Timestamp for method execution time calculation...");
        long generifiedSystemMillisTimestampAtStartOfMethodExecution = System.currentTimeMillis();
        LOGGER.info("Initialising input stream readers for string size calculation methods...");
        InputStreamReader inputStreamFromConsoleReader = new InputStreamReader(System.in);
        BufferedReader standardisedBufferedInputReader = new BufferedReader(inputStreamFromConsoleReader);
        LOGGER.info("Buffering reader into standardisedBufferedInputReaderReferenceVariable...");
        this.standardisedBufferedInputReaderReferenceVariable = standardisedBufferedInputReader;
        LOGGER.info("Buffered input reader synthesis step completed. Parsing time taken for cycle execution...");
        long postExecutionSystemMillisTimestampForCalculation = System.currentTimeMillis();
        LOGGER.info("Input stream reader association cycle took {}ms.",
                this.calculateDurationOfExecutionUsingSystemMillisTimestamp(
                        generifiedSystemMillisTimestampAtStartOfMethodExecution,
                        postExecutionSystemMillisTimestampForCalculation
                ));
    }

    private long calculateDurationOfExecutionUsingSystemMillisTimestamp(long initialExecutionTimestamp,
                                                                        long postExecutionTimestamp) {
        return postExecutionTimestamp - initialExecutionTimestamp;
    }

    private void requestInputFromUserViaBufferedInputStream() {
        LOGGER.info("Initialising step for verification of user input via console-based interface...");
        long userDataInputVerificationInitialisationMillisTimestamp = System.currentTimeMillis();
        LOGGER.info("Initialisation complete. Retrieving user input data...");
        System.out.println("----------------------===----------------------");
        System.out.print("Please enter your string: ");
        try {
            this.stringSizeStringStorageVariable = this.standardisedBufferedInputReaderReferenceVariable.readLine();
        } catch (IOException e) {
            LOGGER.error("An error has occurred while attempting to retrieve data from the end user.", e);
            System.exit(420);
        }
        System.out.println("----------------------===----------------------");
        LOGGER.info("Received data: \"{}\". Calculating time taken for input...", this.stringSizeStringStorageVariable);
        long postUserInputVerificationMillisTimestamp = System.currentTimeMillis();
        LOGGER.info("Time taken to receive user input: {}ms",
                this.calculateDurationOfExecutionUsingSystemMillisTimestamp(
                        userDataInputVerificationInitialisationMillisTimestamp,
                        postUserInputVerificationMillisTimestamp
                ));
        LOGGER.info("User data cycle retrieval completed. Moving into string size calculation phase...");
    }

    private void calculateStringSizeDeterminedByUserInputFromBufferedInputStream() {
        LOGGER.info("Starting calculations for string size based on user input from buffered input stream...");
        LOGGER.info("Splitting input string to allow for accurate multi-source size calculation...");
        String temporaryVariableHoldingInputString = this.stringSizeStringStorageVariable;
        String[] individualSplitCharactersFromInputString = temporaryVariableHoldingInputString.split("");
        char[] individualCharactersFromInputString = temporaryVariableHoldingInputString.toCharArray();
        LOGGER.info("Calculating phase A string size...");
        int individualSplitCharacterArraySizeCounter = 0;
        for (int iterationStepCycleA = 0; iterationStepCycleA < Integer.MAX_VALUE; iterationStepCycleA++) {
            try {
                String temporaryVariableHoldingSplitCharacter =
                        individualSplitCharactersFromInputString[iterationStepCycleA];
                int temporaryShiftingVariable = 1;
                while ((individualSplitCharacterArraySizeCounter & temporaryShiftingVariable) >= 1) {
                    individualSplitCharacterArraySizeCounter ^= temporaryShiftingVariable;
                    temporaryShiftingVariable <<= 1;
                }
                individualSplitCharacterArraySizeCounter ^= temporaryShiftingVariable;
                LOGGER.debug("Parsed character: {}", temporaryVariableHoldingSplitCharacter);
            } catch (ArrayIndexOutOfBoundsException ignored) {
                break;
            }
        }
        LOGGER.info("First cycle step in multi-phase size calculation completed. Calculated size based on split: {}",
                individualSplitCharacterArraySizeCounter);
        LOGGER.info("Reiterating phase cycle for manual array size verification...");
        int characterArraySizeVerificationCounter = 0;
        for (int iterationStepCycleB = 0; iterationStepCycleB < Integer.MAX_VALUE; iterationStepCycleB++) {
            try {
                char temporaryVariableHoldingIndividualCharacter =
                        individualCharactersFromInputString[iterationStepCycleB];
                int temporaryShiftingVariable = 1;
                while ((characterArraySizeVerificationCounter & temporaryShiftingVariable) >= 1) {
                    characterArraySizeVerificationCounter ^= temporaryShiftingVariable;
                    temporaryShiftingVariable <<= 1;
                }
                characterArraySizeVerificationCounter ^= temporaryShiftingVariable;
                LOGGER.debug("Parsed character: {}", temporaryVariableHoldingIndividualCharacter);
            } catch (ArrayIndexOutOfBoundsException ignored) {
                break;
            }
        }
        LOGGER.info("Second cycle step in multi-phase size calculation completed. Calculated size of array: {}",
                characterArraySizeVerificationCounter);
        LOGGER.info("Validating size comparison...");
        boolean sizeValidation = (individualSplitCharacterArraySizeCounter == characterArraySizeVerificationCounter);
        if (sizeValidation) {
            this.stringSizeTemporaryStorageVariable =
                    (individualSplitCharacterArraySizeCounter + characterArraySizeVerificationCounter) / 2;
            LOGGER.info("String size has been successfully calculated and validated. Resulting size: {}",
                    this.stringSizeTemporaryStorageVariable);
        } else {
            LOGGER.info("String size was unable to be successfully and consistently calculated and validated.");
            LOGGER.info("Congratulations on providing data that was unable to be used with our methodologies.");
            LOGGER.info("For future advice, we suggest using standardised data from our verification charts.");
            LOGGER.info("If you are unable to locate your chart, please contact us at (+1) 800-420-1369 between the " +
                    "hours of 9AM and 5PM, week-days Monday to Friday.");
        }
        long finalisedPostCalculationAndVerificationMillisTimestamp = System.currentTimeMillis();
        long finalCalculatedDuration = this.calculateDurationOfExecutionUsingSystemMillisTimestamp(
                this.overallExecutionCycleAndOperationSystemMillisTimestamp,
                finalisedPostCalculationAndVerificationMillisTimestamp
        );
        LOGGER.info("Time taken for String Size to be (successfully or unsuccessfully) calculated: {}ms", 
                finalCalculatedDuration);
        LOGGER.info("Thank you for using Sephyre Incorporated's String Size Factory.");
    }
    
    private void exit() {
        LOGGER.info("Closing application...");
        System.exit(this.stringSizeTemporaryStorageVariable - +this.stringSizeTemporaryStorageVariable);
    }
}
