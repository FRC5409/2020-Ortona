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
    private File       m_file;

    private boolean    m_saved;

    static {
        File logger_dir = new File("home/lvuser/5409/logs/");
        if (!logger_dir.exists())
            logger_dir.mkdirs();
    }

    /**
     * Constructs a new logger and creates a new
     * file.
     * 
     * @param path The path of the file.
     */
    public Logger(String path) {
        m_file = new File("home/lvuser/5409/logs/", path);
            m_file.getParentFile().mkdirs();

        if (!m_file.exists()) try {
            m_file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create log file at \'"+m_file.getPath()+"\'.");
        }

        try {
            m_writer = new FileWriter(m_file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create writer for log file at \'"+m_file.getPath()+"\'.");
        }

        m_saved = false;
    }
    
    /**
     * Writes a string to the logger file.
     * 
     * @param str The data string.
     * 
     * @throws RuntimeException Thrown when the program fails to write
     *                          to the logger file.
     */
    public void write(String str) {
        if (!m_saved) try {
            m_writer.write(str);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to log file at \'"+m_file.getPath()+"\'.");
        }
    }

    /**
     * Writes a formatted string to the logger file.
     * 
     * @param format  The string format.
     * @param args    The string data.
     * 
     * @throws RuntimeException Thrown when the program fails to write
     *                          to the logger file.
     */
    public void write(String format, Object... args) {
        write(String.format(format, args));
    }

    /**
     * Writes a string to the logger file
     * on a newline.
     * 
     * @param str The data string.
     * 
     * @throws RuntimeException Thrown when the program fails to write
     *                          to the logger file.
     */
    public void writeln(String str) {
        write(str + '\n');
    }

    /**
     * Writes a formatted string to the logger file
     * on a newline.
     * 
     * @param format  The string format.
     * @param args    The string data.
     * 
     * @throws RuntimeException Thrown when the program fails to write
     *                          to the logger file.
     */
    public void writeln(String format, Object... args) {
        write(String.format(format, args) + '\n');
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
        if (!m_saved) try {
            m_writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save log file at \'"+m_file.getPath()+"\'.");
        }

        m_saved = true;
    }
}