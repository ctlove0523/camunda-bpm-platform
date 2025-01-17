/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.identity.impl.ldap;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.identity.ldap.util.LdapTestEnvironment;
import org.camunda.bpm.identity.ldap.util.LdapTestEnvironmentRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Represents a test case where the sortControlSupport property is enabled.
 *
 * @author Christopher Zell <christopher.zell@camunda.com>
 */
public class LdapEnableSortControlSupportTest {

  @ClassRule
  public static LdapTestEnvironmentRule ldapRule = new LdapTestEnvironmentRule();
  @Rule
  public ProcessEngineRule engineRule = new ProcessEngineRule("camunda.ldap.enable.sort.control.support.cfg.xml");

  IdentityService identityService;
  LdapTestEnvironment ldapTestEnvironment;

  @Before
  public void setup() {
    identityService = engineRule.getIdentityService();
    ldapTestEnvironment = ldapRule.getLdapTestEnvironment();
  }

  @Test
  public void testOrderByUserFirstName() {
    List<User> orderedUsers = identityService.createUserQuery().orderByUserLastName().asc().list();
    List<User> userList = identityService.createUserQuery().list();

    Collections.sort(userList, new Comparator<User>() {
      @Override
      public int compare(User o1, User o2) {
        return o1.getLastName().compareToIgnoreCase(o2.getLastName());
      }
    });

    int len = orderedUsers.size();
    for (int i = 0; i < len; i++) {
      assertThat(orderedUsers.get(i).getLastName()).as("Index: " + i).isEqualTo(userList.get(i).getLastName());
    }
  }
}
