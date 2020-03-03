package org.frc.team5409.robot.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Simple logging utility. Useful for
 * recording data and information
 * to the roboRio filesystem.
 * 
 * <p> The logger files are located in {@code home/lvuser/5409/logs/} </p>
 * 
 * @author Keith Davies
 */
public final class Logger {
    private FileWriter m_writer;

    private boolean    m_writable;

    static {
        File dir = new File("home/lvuser/5409/logs/");
        if (!dir.exists())
            dir.mkdirs();
    }

    /**
     * Constructs a new logger and creates a new
     * file.
     * 
     * @param path The path of the file.
     */
    public Logger(String path) {
        m_writable = true;

        try  {
            File file = new File("home/lvuser/5409/logs/", path);
                file.getParentFile().mkdirs();
            
            if (!file.exists())
                file.createNewFile();

            m_writer = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
            m_writable = false;
        }
    }
    
    /**
     * Writes a string to the logger file.
     * 
     * @param str The data string.
     * 
     * @return    The logger instance.
     * 
     * @throws RuntimeException Thrown when the program fails to write
     *                          to the logger file.
     */
    public Logger write(String str) {
        if (m_writable) {
            try {
                m_writer.write(str);
            } catch (IOException e) {
                e.printStackTrace();
                m_writable = false;
            }
        }
        return this;
    }

    /**
     * Writes a formatted string to the logger file.
     * 
     * @param format  The string format.
     * @param args    The string data.
     * 
     * @return        The logger instance.
     * 
     * @throws RuntimeException Thrown when the program fails to write
     *                          to the logger file.
     */
    public Logger write(String format, Object... args) {
        return write(String.format(format, args));
    }

    /**
     * Writes a string to the logger file
     * on a newline.
     * 
     * @param str The data string.
     *
     * @return    The logger instance.
     * 
     * @throws RuntimeException Thrown when the program fails to write
     *                          to the logger file.
     */
    public Logger writeln(String str) {
        return write(str + '\n');
    }

    /**
     * Writes a formatted string to the logger file
     * on a newline.
     * 
     * @param format  The string format.
     * @param args    The string data.
     * 
     * @return        The logger instance.
     * 
     * @throws RuntimeException Thrown when the program fails to write
     *                          to the logger file.
     */
    public Logger writeln(String format, Object... args) {
        return write(String.format(format, args) + '\n');
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