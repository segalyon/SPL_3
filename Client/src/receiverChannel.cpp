#include "../include/receiverChannel.h"

void ReceiverChannel::run() {
    bool isRunning = true;
    while (isRunning){
        char opcodeArr[2];
        connection.getBytes(opcodeArr, 2);
        short opcode = bytesToShort(opcodeArr);
        ///
        std::string msg;
        // NOTIFICATION
        if (opcode == 9) {
            msg = "NOTIFICATION";
            // public/PM
            char PMorPublic[1];
            connection.getBytes(PMorPublic, 1);
            short pmOrPublic = bytesToShort(PMorPublic);
            if(pmOrPublic==2)
                msg += " Public";
            else
                msg += " PM";
            // username
            std::string username;
            connection.getFrameAscii(username, '\0');
            msg = msg + " " + username;
            // content
            std::string content;
            connection.getFrameAscii(content, '\0');
            msg = msg + " " + content;
        }
        // ACK
        if (opcode == 10) {
            char msgOpcodeArr[2];
            connection.getBytes(msgOpcodeArr, 2);
            short msgOpcode = bytesToShort(msgOpcodeArr);
            msg = "ACK " + std::to_string(msgOpcode);
            //
            if(msgOpcode == 7 || msgOpcode == 8){
                char ageArr[2];
                connection.getBytes(ageArr, 2);
                short age = bytesToShort(ageArr);
                //
                char numPostsArr[2];
                connection.getBytes(numPostsArr, 2);
                short numPosts = bytesToShort(numPostsArr);
                //
                char numFollowersArr[2];
                connection.getBytes(numFollowersArr, 2);
                short numFollowers = bytesToShort(numFollowersArr);
                //
                char numFollowingArr[2];
                connection.getBytes(numFollowingArr, 2);
                short numFollowing = bytesToShort(numFollowingArr);
                msg += " " + std::to_string(age) + " " + std::to_string(numPosts) + " " + std::to_string(numFollowers) + " " + std::to_string(numFollowing);
            }
            if(msgOpcode == 4){
                char followUnfollowArr[1];
                connection.getBytes(followUnfollowArr, 2);
                short followUnfollow = bytesToShort(followUnfollowArr);
                std::string username;
                connection.getFrameAscii(username, '\0');
                msg += " " + std::to_string(followUnfollow) + " " + username;
            }

            string optionalArg;
        }
        // ERROR
        if (opcode == 11) {
            char errorArr[2];
            connection.getBytes(errorArr, 2);
            short error = bytesToShort(errorArr);
            msg = "ERROR " + std::to_string(error);
        }

        // print
        cout<<msg<<endl;

        // clean msg
        //char cleanArr[1];
        //while(connection.getBytes(cleanArr, 1)){
        //}

        // login/logout
        if(msg == "ACK 2") {
            loginState = true;
        }
        if(msg == "ACK 3") {
            isRunning = false;
            connection.close();
        }
    }


}