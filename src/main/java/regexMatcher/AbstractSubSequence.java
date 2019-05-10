package regexMatcher;


public abstract class AbstractSubSequence {
    // CSR = CoreSubRegex
    int startingSubSeqIndex;
    int numberOfSubSequences;
    int originalSubSeqsCount;
    // int originalSubCount;
    // spans belong to the matches in the latest tuple



    // Structure for keeping statistics
    //RegexStats stats = null;

    public AbstractSubSequence(int start, int length){
        startingSubSeqIndex = start;
        numberOfSubSequences = length;
    }

    public int getOriginalSubSeqsCount(){
        return originalSubSeqsCount;
    }
    public void setOriginalSubCount(int count){
        originalSubSeqsCount = count;
    }

    public int getStart(){
        return startingSubSeqIndex;
    }
    public int getEnd(){
        return startingSubSeqIndex + numberOfSubSequences;
    }

    public boolean isFirstSubSequence(){
        return getStart() == 0;
    }

    public boolean isLastSubSequence(){
        return getEnd() == getOriginalSubSeqsCount();
    }


    public String toStringShort(){
        return "[" + getStart() + "," + getEnd() + ") ";
    }

    public abstract boolean isSubRegex();


}

