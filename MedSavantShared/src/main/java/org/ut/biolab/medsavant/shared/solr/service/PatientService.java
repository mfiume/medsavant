/*
 *    Copyright 2011-2012 University of Toronto
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.ut.biolab.medsavant.shared.solr.service;

import org.ut.biolab.medsavant.shared.model.Patient;
import org.ut.biolab.medsavant.shared.util.Entity;

/**
 * Manager indexing of Patient objects.
 */
public class PatientService extends AbstractSolrService<Patient> {

    @Override
    protected String getName() {
        return Entity.PATIENT;
    }

}