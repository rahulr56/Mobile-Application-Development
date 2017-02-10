package com.chatroom.inclass09;

import java.util.List;

/**
 * Created by Raghavan on 31-Oct-16.
 */

public class Message {

    /**
     * messages : [{"UserFname":"Alice","UserLname":"Tom","Id":"64","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 01:00:46"},{"UserFname":"M","UserLname":"J","Id":"63","Comment":"boooooooooooooooo","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:59:40"},{"UserFname":"Alice","UserLname":"Tom","Id":"62","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:59:07"},{"UserFname":"M","UserLname":"J","Id":"61","Comment":"boooooooooooooooo","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:59:00"},{"UserFname":"FirstNameTest","UserLname":"LastNameTest","Id":"60","Comment":"Testing message !!!!","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:57:57"},{"UserFname":"M","UserLname":"J","Id":"59","Comment":"boooooooooooooooo","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:57:47"},{"UserFname":"Alice","UserLname":"Tom","Id":"58","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:57:39"},{"UserFname":"M","UserLname":"J","Id":"57","Comment":"boooooooooooooooo","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:57:13"},{"UserFname":"Alice","UserLname":"Tom","Id":"56","Comment":"HELLO","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:56:46"},{"UserFname":"Alice","UserLname":"Tom","Id":"55","Comment":"This is the third text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:55:44"},{"UserFname":"Alice","UserLname":"Tom","Id":"54","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:55:26"},{"UserFname":"Matt","UserLname":"Corbett","Id":"53","Comment":"don't take franz rothe SERIOUSLY!!!","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:51:55"},{"UserFname":"Matt","UserLname":"Corbett","Id":"52","Comment":"don't take franz rothe SERIOUSLY!!!","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:50:32"},{"UserFname":"M","UserLname":"J","Id":"51","Comment":"Test 1 2 3 4 5 6","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:50:24"},{"UserFname":"Matt","UserLname":"Corbett","Id":"50","Comment":"don't take franz rothe SERIOUSLY!!!","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:50:15"},{"UserFname":"Matt","UserLname":"Corbett","Id":"49","Comment":"don't take franz rothe SERIOUSLY!!!","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:49:18"},{"UserFname":"Matt","UserLname":"Corbett","Id":"48","Comment":"don't take franz rothe SERIOUSLY!!!","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:48:44"},{"UserFname":"FirstNameTest","UserLname":"LastNameTest","Id":"47","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:47:59"},{"UserFname":"Alice","UserLname":"Tom","Id":"46","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:47:37"},{"UserFname":"Alice","UserLname":"Tom","Id":"45","Comment":"This is the third text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:46:21"},{"UserFname":"Matt","UserLname":"Corbett","Id":"44","Comment":"don't take franz rothe SERIOUSLY!!!","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:45:29"},{"UserFname":"Matt","UserLname":"Corbett","Id":"43","Comment":"don't take franz rothe SERIOUSLY!!!","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:44:54"},{"UserFname":"Alice","UserLname":"Tom","Id":"42","Comment":null,"FileThumbnailId":"sCgOGHS","Type":"IMAGE","CreatedAt":"2016-11-01 00:43:31"},{"UserFname":"tester","UserLname":"tester","Id":"41","Comment":"a new msg","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:42:31"},{"UserFname":"Alice","UserLname":"Tom","Id":"40","Comment":"Hi Hello; Kishore","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:39:16"},{"UserFname":"Alice","UserLname":"Tom","Id":"39","Comment":"this is a message from android","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:37:51"},{"UserFname":"Alice","UserLname":"Tom","Id":"38","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:36:51"},{"UserFname":"Alice","UserLname":"Tom","Id":"37","Comment":"Hi Hello; Kishore","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:36:08"},{"UserFname":"Alice","UserLname":"Tom","Id":"36","Comment":"Hi Hello; Kishore","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:34:59"},{"UserFname":"Alice","UserLname":"Tom","Id":"35","Comment":"Hi Hello; Kishore","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:34:04"},{"UserFname":"Alice","UserLname":"Tom","Id":"34","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:33:44"},{"UserFname":"Alice","UserLname":"Tom","Id":"33","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:33:42"},{"UserFname":"Alice","UserLname":"Tom","Id":"32","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:32:56"},{"UserFname":"Matt","UserLname":"Corbett","Id":"31","Comment":"don't take franz rothe SERIOUSLY!!!","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:32:07"},{"UserFname":"Matt","UserLname":"Corbett","Id":"30","Comment":"don't take franz rothe SERIOUSLY!!!","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:31:50"},{"UserFname":"Matt","UserLname":"Corbett","Id":"29","Comment":"don't take franz rothe SERIOUSLY!!!","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:30:28"},{"UserFname":"Matt","UserLname":"Corbett","Id":"28","Comment":"don't take franz rothe SERIOUSLY!!!","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:28:59"},{"UserFname":"Matt","UserLname":"Corbett","Id":"27","Comment":"don't take franz rothe SERIOUSLY!!!","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:28:34"},{"UserFname":"Matt","UserLname":"Corbett","Id":"26","Comment":"don't take franz rothe SERIOUSLY!!!","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:27:57"},{"UserFname":"Alice","UserLname":"Tom","Id":"25","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:27:45"},{"UserFname":"Alice","UserLname":"Tom","Id":"24","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:26:47"},{"UserFname":"Matt","UserLname":"Corbett","Id":"23","Comment":"don't take franz rothe SERIOUSLY!!!","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:26:10"},{"UserFname":"Alice","UserLname":"Tom","Id":"22","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:25:50"},{"UserFname":"Alice","UserLname":"Tom","Id":"21","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:25:44"},{"UserFname":"Matt","UserLname":"Corbett","Id":"20","Comment":"don't take franz rothe SERIOUSLY!!!","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:25:09"},{"UserFname":"Alice","UserLname":"Tom","Id":"19","Comment":"This is lokesh","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:21:57"},{"UserFname":"Alice","UserLname":"Tom","Id":"18","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:21:07"},{"UserFname":"Alice","UserLname":"Tom","Id":"17","Comment":"Hi Akarsh","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:19:00"},{"UserFname":"Alice","UserLname":"Tom","Id":"16","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:18:30"},{"UserFname":"Alice","UserLname":"Tom","Id":"15","Comment":"Hi Akarsh","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:18:25"},{"UserFname":"Alice","UserLname":"Tom","Id":"14","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:18:14"},{"UserFname":"Alice","UserLname":"Tom","Id":"13","Comment":"hey hi","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:14:26"},{"UserFname":"Alice","UserLname":"Tom","Id":"12","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:14:18"},{"UserFname":"Matt","UserLname":"Corbett","Id":"11","Comment":"don't take franz rothe SERIOUSLY!!!","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:14:14"},{"UserFname":"Alice","UserLname":"Tom","Id":"10","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:13:42"},{"UserFname":"Matt","UserLname":"Corbett","Id":"9","Comment":"This is a message i think","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:12:09"},{"UserFname":"Alice","UserLname":"Tom","Id":"8","Comment":"Hey","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:11:05"},{"UserFname":"Alice","UserLname":"Tom","Id":"7","Comment":"Hey","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:09:30"},{"UserFname":"Alice","UserLname":"Tom","Id":"6","Comment":"This is the third text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:08:44"},{"UserFname":"Alice","UserLname":"Tom","Id":"5","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:08:38"},{"UserFname":"Alice","UserLname":"Tom","Id":"4","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:06:14"},{"UserFname":"Alice","UserLname":"Tom","Id":"3","Comment":"Hello","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:04:27"},{"UserFname":"Alice","UserLname":"Tom","Id":"2","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-11-01 00:04:19"},{"UserFname":"Alice","UserLname":"Tom","Id":"1","Comment":"This is the second text message","FileThumbnailId":null,"Type":"TEXT","CreatedAt":"2016-10-31 23:58:24"}]
     * status : ok
     */

    private String status;
    /**
     * UserFname : Alice
     * UserLname : Tom
     * Id : 64
     * Comment : This is the second text message
     * FileThumbnailId : null
     * Type : TEXT
     * CreatedAt : 2016-11-01 01:00:46
     */

    private List<MessagesBean> messages;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MessagesBean> getMessages() {
        return messages;
    }

    public void setMessages(List<MessagesBean> messages) {
        this.messages = messages;
    }

    public static class MessagesBean {
        private String UserFname;
        private String UserLname;
        private String Id;
        private String Comment;
        private Object FileThumbnailId;
        private String Type;
        private String CreatedAt;

        public String getUserFname() {
            return UserFname;
        }

        public void setUserFname(String UserFname) {
            this.UserFname = UserFname;
        }

        public String getUserLname() {
            return UserLname;
        }

        public void setUserLname(String UserLname) {
            this.UserLname = UserLname;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getComment() {
            return Comment;
        }

        public void setComment(String Comment) {
            this.Comment = Comment;
        }

        public Object getFileThumbnailId() {
            return FileThumbnailId;
        }

        public void setFileThumbnailId(Object FileThumbnailId) {
            this.FileThumbnailId = FileThumbnailId;
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
        }

        public String getCreatedAt() {
            return CreatedAt;
        }

        public void setCreatedAt(String CreatedAt) {
            this.CreatedAt = CreatedAt;
        }
    }
}
