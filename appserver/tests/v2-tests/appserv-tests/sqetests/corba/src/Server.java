/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package corba;

import org.omg.CORBA.ORB;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.Policy;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NameComponent;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.ForwardRequest;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.ServantLocator;
import org.omg.PortableServer.RequestProcessingPolicyValue;
import org.omg.PortableServer.ServantRetentionPolicyValue;
import org.omg.PortableServer.ServantLocatorPackage.CookieHolder;

import javax.rmi.PortableRemoteObject;

import com.sun.ejte.ccl.reporter.SimpleReporterAdapter;
public class Server {

    private static SimpleReporterAdapter status =
         new SimpleReporterAdapter("appserv-tests");

    public static void main(String[] args) {
        final String suiteId = "CORBA";
        final String testId = "rmiiiop test";

        try {

            status.addDescription("To test registration, resolution and " +
                                  "remote invocation on a rmiiiop server");

            ORB orb = ORB.init(args, System.getProperties());
            System.out.println("ORB Initialized.");

            POA root = POAHelper.narrow(
                orb.resolve_initial_references("RootPOA"));

            Policy[] policy = new Policy[2];
            policy[0] = root.create_request_processing_policy(
                RequestProcessingPolicyValue.USE_SERVANT_MANAGER);
            policy[1] = root.create_servant_retention_policy(
                ServantRetentionPolicyValue.NON_RETAIN);
            POA slpoa = root.create_POA("NR_USM_POA", null, policy);
            slpoa.set_servant_manager(new MyServantLocator());

            Servant servant = getServant();
            String intf = servant._all_interfaces(slpoa, null)[0];
            byte[] oid = "abcd".getBytes();
            org.omg.CORBA.Object ref =
                slpoa.create_reference_with_id(oid, intf);

            root.the_POAManager().activate();
            slpoa.the_POAManager().activate();

            NamingContext namingContext = NamingContextHelper.narrow(
                orb.resolve_initial_references("NameService"));
            System.out.println(namingContext);

            System.out.println("Resolved NameService");


            NameComponent[] names = { new NameComponent("RemoteTest", "") };
            namingContext.rebind(names, ref);
            System.out.println("rebind nameservice");

            ORB clientORB = ORB.init(args, System.getProperties());
            System.out.println(args[0]);
            RemoteTest testRef = (RemoteTest) PortableRemoteObject.narrow(
                clientORB.string_to_object(args[0]), RemoteTest.class);
            System.out.println("Pinging... ");
            testRef.ping();

            status.addStatus(testId, status.PASS);
        } catch (Throwable e) {
            status.addStatus(testId, status.FAIL);
            System.out.println("If You're using Mainstream build, "+
                               "please check the LD_LIBRARY_PATH for "+
                               "solaris and PATH for windows.\n"+
                               "Include /usr/lib/mps in the LD_LIBRARY_PATH "+
                               "for solaris");
            e.printStackTrace();
        }
        status.printSummary("corbaID");
        System.out.println("Test done. exiting");
        System.exit(0);
    }

    public static Servant getServant() {
        Servant servant = null;
        try {
            RemoteTestImpl remoteTest = new RemoteTestImpl();
            servant =  (Servant) javax.rmi.CORBA.Util.getTie(remoteTest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return servant;
    }
}

class MyServantLocator extends LocalObject implements ServantLocator {

    public Servant preinvoke(byte[] oid, POA adapter, String name,
                             CookieHolder cookie)
    throws ForwardRequest {
        return Server.getServant();
    }

    public void postinvoke(byte[] oid, POA adapter, String name,
                           java.lang.Object obj, Servant servant)
    {

    }

}
