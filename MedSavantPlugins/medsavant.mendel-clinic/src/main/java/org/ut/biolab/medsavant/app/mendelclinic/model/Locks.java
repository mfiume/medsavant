/**
 * Copyright (c) 2014 Marc Fiume <mfiume@cs.toronto.edu>
 * Unauthorized use of this file is strictly prohibited.
 * 
 * All rights reserved. No warranty, explicit or implicit, provided.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, TITLE AND NON-INFRINGEMENT. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDERS OR ANYONE DISTRIBUTING THE SOFTWARE BE LIABLE
 * FOR ANY DAMAGES OR OTHER LIABILITY, WHETHER IN CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package org.ut.biolab.medsavant.app.mendelclinic.model;

import java.io.File;
import javax.swing.JDialog;

/**
 *
 * @author mfiume
 */
public class Locks {

    public static class DialogLock {

        private JDialog resultsDialog;

        public JDialog getResultsDialog() {
            return resultsDialog;
        }

        public void setResultsFrame(JDialog resultsDialog) {
            this.resultsDialog = resultsDialog;
        }
    }

    public static class FileResultLock {

        File f;

        public File getFile() {
            return f;
        }

        public void setFile(File f) {
            this.f = f;
        }
    }
}
