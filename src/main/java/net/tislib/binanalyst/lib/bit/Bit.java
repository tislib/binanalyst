package net.tislib.binanalyst.lib.bit;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public interface Bit {

    public BinaryValue getValue();

    default int intVal() {
        return getValue().isTrue() ? 1 : 0;
    }

    String getType();
}
