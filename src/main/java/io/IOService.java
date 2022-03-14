package io;

import java.io.IOException;

public interface IOService {
    InputDTO inputData() throws IOException;
    void printData(AnswerDTO answerDTO);
}
