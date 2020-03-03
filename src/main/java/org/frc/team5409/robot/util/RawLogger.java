package org.frc.team5409.robot.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;

/**
 * Simple logging utility. Useful for
 * recording bulk raw data and information
 * to the roboRio filesystem.
 * 
 * <p> The logger files are located in {@code home/lvuser/5409/logs/} </p>
 * 
 * @author Keith Davies
 */
public final class RawLogger {
    private OutputStream m_writer;

    private boolean      m_writable;

    static {
        File dir = new File("home/lvuser/5409/logs/raw/");
        if (!dir.exists())
            dir.mkdirs();
    }

    /**
     * Constructs a new logger and creates a new
     * file.
     * 
     * @param path The path of the file.
     */
    public RawLogger(String path) {
        m_writable = true;

        try  {
            File file = new File("home/lvuser/5409/logs/raw/", path);
                file.getParentFile().mkdirs();
            
            if (!file.exists())
                file.createNewFile();

            m_writer = new FileOutputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
            m_writable = false;
        }
    }
    
    public RawLogger write(byte data[]) {
        if (m_writable) {
            try {
                m_writer.write(data);
            } catch (IOException e) {
                e.printStackTrace();
                m_writable = false;
            }
        }
        return this;
    }

    /**
     * Writes bytes to the logger file.
     * 
     * @param str The data string.
     * 
     * @return    The logger instance.
     * 
     * @throws RuntimeException Thrown when the program fails to write
     *                          to the logger file.
     */
    public RawLogger write(byte data) {
        return write(new byte[] {data});
    }

    public RawLogger write(short data) {
        return write(new byte[] {(byte) ((data >> 8 ) & 0xFF),
                                 (byte) ((data      )       )});
    }

    public RawLogger write(int data) {
        return write(new byte[] {(byte) ((data >> 24) & 0xFF),
                                 (byte) ((data >> 16) & 0xFF),
                                 (byte) ((data >> 8 ) & 0xFF),
                                 (byte) ((data      ) & 0xFF)});
    }

    public RawLogger write(long data) {
        return write(new byte[] {(byte) ((data >> 56) & 0xFF),
                                 (byte) ((data >> 48) & 0xFF),
                                 (byte) ((data >> 40) & 0xFF),
                                 (byte) ((data >> 32) & 0xFF),
                                 (byte) ((data >> 24) & 0xFF),
                                 (byte) ((data >> 16) & 0xFF),
                                 (byte) ((data >> 8 ) & 0xFF),
                                 (byte) ((data      )       )});
    }

    public RawLogger write(float data) {
        return write(Float.floatToIntBits(data));
    }

    public RawLogger write(double data) {
        return write(Double.doubleToLongBits(data));
    }

    /**
     * Saves the logger file to the roboRio
     * filesystem.
     * 
     * <p> Once a logger file is saved, it cannot be
     * modified. Any subsequent calls to {@code write()}
     * will result in no changes. </p>
     */
    public void save() {
        if (m_writable) {
            try {
                m_writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        m_writable = false;
    }
}