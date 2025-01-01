import { get, post } from '/@/utils/http/axios';

enum URL {
    listUserDialogs = '/api/dialog/list',          // 获取用户所有对话
    createDialog = '/api/dialog/create',           // 创建对话
    listMessages = '/api/dialog/messages',        // 获取对话中的所有消息
    sendMessage = '/api/dialog/message/send',     // 发送消息
    deleteMessage = '/api/dialog/message/delete', // 删除消息
    deleteDialog = '/api/dialog/delete'           // 删除对话
}

// 获取某个用户参与的所有对话
const listUserDialogsApi = async (userId: any) => {
    return get<any>({
        url: URL.listUserDialogs,
        params: { userId }, // 通过查询参数传递 userId
        data: {},
        headers: {}
    });
};

// 创建对话
const createDialogApi = async (user1Id: any, user2Id: any) => {
    console.log('user1Id:', user1Id, 'user2Id:', user2Id); // 添加日志
    return post<any>({
        url: URL.createDialog,
        params: { user1Id, user2Id }, // 通过查询参数传递
        data: {},
        headers: { 'Content-Type': 'application/json;charset=utf-8' }
    });
};


// 获取某个对话的所有消息
const listMessagesApi = async (dialogId: any) => {
    return get<any>({
        url: URL.listMessages,
        params: { dialogId }, // 通过查询参数传递 dialogId
        data: {},
        headers: {}
    });
};
// 发送消息
const sendMessageApi = async (dialogId: any, senderId: any, content: any) => {
    return post<any>({
        url: URL.sendMessage,
        params: { dialogId, senderId, content }, // 通过查询参数传递 dialogId, senderId, 和 content
        data: {},
        headers: { 'Content-Type': 'application/json;charset=utf-8' }
    });
};


// 删除消息
const deleteMessageApi = async (messageId: any) => {
    return post<any>({
        url: URL.deleteMessage,
        params: { messageId }, // 通过查询参数传递 messageId
        data: {},
        headers: { 'Content-Type': 'application/json;charset=utf-8' }
    });
};


// 删除对话
const deleteDialogApi = async (dialogId: any) => {
    return post<any>({
        url: URL.deleteDialog,
        params: { dialogId }, // 通过查询参数传递 dialogId
        data: {},
        headers: { 'Content-Type': 'application/json;charset=utf-8' }
    });
};



export {
    listUserDialogsApi,
    createDialogApi,
    listMessagesApi,
    sendMessageApi,
    deleteMessageApi,
    deleteDialogApi
};
