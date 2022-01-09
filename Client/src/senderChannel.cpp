//
// Created by roy on 12/28/21.
//

#include "../include/senderChannel.h"
#include <iostream>

void SenderChannel::run() {
    bool isRunning = true;
    while(isRunning){
        //char opcode[2];
        std::string inputLine = "";
        getline(cin, inputLine);
        vector<std::string> inputVectors = split(inputLine, " ");

        std::string actionName = inputVectors[0];
        short opcode;
        char byteArr[2];
        ///
        if (actionName == "REGISTER") {
            opcode = 1;
            shortToBytes(opcode, byteArr);
            connection.sendBytes(byteArr, 2);
            connection.sendFrameAscii(inputVectors[1], '\0');
            connection.sendFrameAscii(inputVectors[2], '\0');
            connection.sendFrameAscii(inputVectors[3], '\0');
        }
        ///
        if (actionName == "LOGIN") {
            short opcode = 2;
            shortToBytes(opcode, byteArr);
            connection.sendBytes(byteArr, 2);
            connection.sendFrameAscii(inputVectors[1], '\0');
            connection.sendFrameAscii(inputVectors[2], '\0');

            // send captcha at the end without separator at the end
            char captchaArr[1];
            short captcha;
            if (inputVectors.size() > 3){
                captcha = (short)stoi(inputVectors[3]);
            }
            else{
                captcha = 0;
            }
            captchaArr[0]=captcha;
            connection.sendBytes(captchaArr, 1);
        }
        ///
        if (actionName == "LOGOUT") {
            short opcode = 3;
            shortToBytes(opcode, byteArr);
            connection.sendBytes(byteArr, 2);
            // if user is logged, then logout should terminate thread
            if(loginState) {
                isRunning = false;
            }
        }
        ///
        if (actionName == "FOLLOW") {
            short opcode = 4;
            shortToBytes(opcode, byteArr);
            connection.sendBytes(byteArr, 2);
            // send follow/unfollow without separator at the end
            char followArr[1];
            short follow = (short)stoi(inputVectors[1]);
            followArr[0] = (short)follow;
            connection.sendBytes(followArr, 1);
            //
            connection.sendFrameAscii(inputVectors[2].c_str(), '\0');
        }
        ///
        if (actionName == "POST") {
            short opcode = 5;
            shortToBytes(opcode, byteArr);
            connection.sendBytes(byteArr, 2);
            // merge all post to one string
            string merge = "";
            if(inputVectors.size() > 1) {
                merge = inputVectors[1];
                for (int i = 2; i < inputVectors.size(); ++i) {
                    merge += " " + inputVectors[i];
                }
            }
            connection.sendFrameAscii(merge, '\0');
        }
        ///
        if (actionName == "PM") {
            short opcode = 6;
            shortToBytes(opcode, byteArr);
            connection.sendBytes(byteArr, 2);
            // send username
            connection.sendFrameAscii(inputVectors[1], '\0');
            // send content
            string content = " ";
            if(inputVectors.size() > 2) {
                content = inputVectors[2];
                for (int i = 3; i < inputVectors.size(); ++i) {
                    content += " " + inputVectors[i];
                }
            }
            connection.sendFrameAscii(content, '\0');
            // send current date
            connection.sendFrameAscii(getCurrentDatetime(), '\0');
        }
        ///
        if (actionName == "LOGSTAT") {
            short opcode = 7;
            shortToBytes(opcode, byteArr);
            connection.sendBytes(byteArr, 2);
        }
        ///
        if (actionName== "STAT") {
            short opcode = 8;
            shortToBytes(opcode, byteArr);
            connection.sendBytes(byteArr, 2);
            //
            connection.sendFrameAscii(inputVectors[1], '\0');
        }
        ///
        if (actionName== "BLOCK") {
            short opcode = 12;
            shortToBytes(opcode, byteArr);
            connection.sendBytes(byteArr, 2);
            //
            connection.sendFrameAscii(inputVectors[1], '\0');
        }

        string s = ";";
        connection.sendBytes(s.c_str(), 1);
        inputVectors.clear();
    }
}