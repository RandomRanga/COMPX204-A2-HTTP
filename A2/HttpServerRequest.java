class HttpServerRequest
{
    private String file = null;
    private String host = null;
    private boolean done = false;
    private int line = 0;

    public boolean isDone() { return done; }
    public String getFile() { return file; }
    public String getHost() { return host; }

    public void process(String in)
    {
	/*
	 * process the line, setting 'done' when HttpServerSession should
	 * examine the contents of the request using getFile and getHost
	 */
        String parts[] = in.split(" ");
        String filename = "";

        if(parts[0].compareTo("GET") == 0){
            filename = parts[1].substring(1);
        }

        if(parts[0].compareTo("Host:") == 0){
            String hostname = parts[1].substring(4);
        }

        if(filename.endsWith("/")){
            filename += "index.html";
        }

        

        line++;




    }
}
