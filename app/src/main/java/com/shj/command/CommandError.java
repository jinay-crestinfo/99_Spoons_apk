package com.shj.command;

/* loaded from: classes2.dex */
public class CommandError {
    private String info;
    private CommandErrorType type;

    /* loaded from: classes2.dex */
    public enum CommandErrorType {
        UpError("上位机接收并处理结果时出错", 0),
        DownErro("下位机处理时出错", 1),
        TimeOut("超时", 2);

        private int index;
        private String name;

        CommandErrorType(String str, int i) {
            setName(str);
            setIndex(i);
        }

        public String getName() {
            return this.name;
        }

        public void setName(String str) {
            this.name = str;
        }

        public int getIndex() {
            return this.index;
        }

        public void setIndex(int i) {
            this.index = i;
        }

        public static CommandErrorType int2Enum(int i) {
            CommandErrorType commandErrorType = UpError;
            for (CommandErrorType commandErrorType2 : values()) {
                if (commandErrorType2.getIndex() == i) {
                    return commandErrorType2;
                }
            }
            return commandErrorType;
        }
    }

    public CommandError(CommandErrorType commandErrorType, String str) {
    }

    public CommandErrorType getType() {
        return this.type;
    }

    public void setType(CommandErrorType commandErrorType) {
        this.type = commandErrorType;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String str) {
        this.info = str;
    }
}
