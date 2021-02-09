package com.cc.sdk2.jsdk.base.net;

import java.util.Date;

/**
 * All rights reserved, copyright@cc.hu
 * 网络通信的协议基础接口
 *
 * @author cc
 * @version 1.0
 * @date 2020/3/14 16:28
 **/
public abstract class AbstractProtocol<T> {

    protected abstract void writeByte(byte b);

    protected abstract void writeCharacter(char ch);

    protected abstract void writeString(String str);

    protected abstract void writeBoolean(boolean bool);

    protected abstract void writeInt16(short i16);

    protected abstract void writeInt32(int i32);

    protected abstract void writeInt64(long i64);

    protected abstract void writeDouble(double dou);

    protected abstract void writeDate(Date date);

    protected abstract byte readByte();

    protected abstract char readCharacter();

    protected abstract String readString();

    protected abstract boolean readBoolean();

    protected abstract short readInt16();

    protected abstract int readInt32();

    protected abstract long readInt64();

    protected abstract double readDouble();

    protected abstract Date readDate();

    public abstract void writeMsg(T msg);

    public abstract T readMsg();

}
