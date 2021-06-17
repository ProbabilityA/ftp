package com.pa.ftpserver.ftp.task;

import com.pa.ftpserver.ftp.constant.ResponseMessage;

import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * @author pa
 * @date 2021/6/17 22:52
 */
public abstract class AbstractDataTransferTask implements DataTransferTask {

    protected AbstractDataTransferTask(String principal) {
        WeakReference<DefaultCommunicateTask> weakReference = DefaultCommunicateTask.taskMap.get(principal);
        this.startHook = (ignore) -> {
            DefaultCommunicateTask communicateTask = weakReference.get();
            if (communicateTask != null) {
                communicateTask.sendMessage(ResponseMessage.OPEN_CHANNEL);
            }
        };
        this.finishHook = (ignore) -> {
            DefaultCommunicateTask communicateTask = weakReference.get();
            if (communicateTask != null) {
                communicateTask.sendMessage(ResponseMessage.TRANSFER_COMPLETE);
            }
        };
    }

    protected Consumer<Socket> consumer;

    protected Consumer<Void> startHook;

    protected Consumer<Void> finishHook;

    @Override
    public void setSocketConsumer(Consumer<Socket> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void setStartHook(Consumer<Void> consumer) {
        this.startHook = consumer;
    }

    @Override
    public void setFinishHook(Consumer<Void> consumer) {
        this.finishHook = consumer;
    }
}
