package it.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;


@WebServlet("/view")
public class view extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final static Logger log = Logger.getLogger(view.class.getName());

	private String[] command;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public view() {
		super();
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		this.command = servletConfig.getInitParameter("cmd").split("\\s");
	}


	// TODO: extend the functionality to the get requests
	
	/**
	 * @throws IOException
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		log.info("[" + this.command[0] + "]Serving Post request");

		// configuring the ProcessBuilder with the command str
		ProcessBuilder builder = new ProcessBuilder(command);

		// configuring the process builder
		builder.redirectInput(Redirect.PIPE);
		builder.redirectOutput(Redirect.PIPE);

		// configuring process stderr to be redirected to the log system
		builder.redirectError(Redirect.PIPE);

		Process p = null;
		try {
			log.trace("Starting the sub process");

			p = builder.start();
			
			log.trace("Process has been started");
		} catch (IOException e) {

			// building the command str for passing it to the logger
			StringBuilder c = new StringBuilder();
			for (int i = 0; i < command.length; i++) {
				c.append(command[i]);
				c.append("[SPACE]");
			}
			log.fatal(
					"Failed the command execution:\nCommand:\n" + c.toString(),
					e);
			return;
		}


		log.trace("Copying the http request data to the subprocess input");

		// copying the http request data to the subprocess input
		OutputStream sub_process_input = p.getOutputStream();
		ServletInputStream request_data = request.getInputStream();
		IOUtils.copy(request_data, sub_process_input);
		sub_process_input.close();
		request_data.close();

		log.trace("Copying the output of the subprocess in the http response");

		// copying the output of the subprocess in the http response
		InputStream sub_process_output = p.getInputStream();
		ServletOutputStream response_data = response.getOutputStream();
		IOUtils.copy(sub_process_output, response_data);
		sub_process_output.close();
		response_data.close();
		
		log.trace("Copying the error stream of the process (if there exists) to the log system");

		String procErr = IOUtils.toString(p.getErrorStream(), "UTF-8");
		p.getErrorStream().close();

		if (procErr != null)
			if (!procErr.equals(""))
				log.fatal(procErr);

		log.debug("Served Post request");
	}
}
