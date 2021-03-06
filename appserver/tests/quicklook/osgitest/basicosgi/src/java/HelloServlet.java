/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
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

package osgitest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Simple servlet to validate that the Hello, World example can
 * execute servlets.  In the web application deployment descriptor,
 * this servlet must be mapped to correspond to the link in the
 * "index.html" file.
 *
 * @author Craig R. McClanahan <Craig.McClanahan@eng.sun.com>
 */

public final class HelloServlet extends HttpServlet {


    /**
     * Respond to a GET request for the content produced by
     * this servlet.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are producing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
      throws IOException, ServletException {

          System.out.println("Servlet processing do get..");

    response.setContentType("text/html");
    PrintWriter writer = response.getWriter();

    writer.println("<html>");
    writer.println("<head>");
    writer.println("<title>Sample Application Servlet Page</title>");
    writer.println("</head>");
    writer.println("<body bgcolor=white>");

    writer.println("<table border=\"0\">");
    writer.println("<tr>");
    writer.println("<td>");
    //writer.println("<img src=\"images/tomcat.gif\">");
    writer.println("</td>");
    writer.println("<td>");
    writer.println("<h1>Sample Application Servlet</h1>");
    writer.println("This is the output of a servlet that is part of");
    writer.println("the Hello, World application.  It displays the");
    writer.println("request headers from the request we are currently");
    writer.println("processing.");
    writer.println("</td>");
    writer.println("</tr>");
    writer.println("</table>");

    writer.println("<table border=\"0\" width=\"100%\">");
    Enumeration names = request.getHeaderNames();
    while (names.hasMoreElements()) {
        String name = (String) names.nextElement();
        writer.println("<tr>");
        writer.println("  <th align=\"right\">" + name + ":</th>");
        writer.println("  <td>" + request.getHeader(name) + "</td>");
        writer.println("</tr>");
    }
    writer.println("</table>");

    writer.println("</body>");
    writer.println("</html>");

    }


}

