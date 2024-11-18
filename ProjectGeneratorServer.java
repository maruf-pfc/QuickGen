import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.*;

public class ProjectGeneratorServer {
    private static final int PORT = 6000;
    private static final String TEMPLATES_DIR = "templates";

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                String technology = in.readLine();
                String projectName = in.readLine();
                String directory = in.readLine();

                String response = generateProject(technology, projectName, directory);
                out.println(response);
            } catch (IOException e) {
                System.err.println("Client handler exception: " + e.getMessage());
            }
        }

        private String generateProject(String technology, String projectName, String directory) {
            try {
                String templateDir = mapTechnologyToTemplate(technology);
                Path templatePath = Paths.get(templateDir);
                
                if (!Files.exists(templatePath)) {
                    return "Error: Template directory not found for " + technology + ". Please check the server configuration.";
                }

                Path projectDir = Paths.get(directory, projectName);
                Files.createDirectories(projectDir);

                copyTemplateFiles(templatePath, projectDir);
                generateReadme(projectDir, technology, projectName);

                return "Project created successfully at: " + projectDir.toString() + "\n" +
                       "Please check the README.md file for instructions on how to run the project.";
            } catch (IOException e) {
                return "Error generating project: " + e.getMessage() + "\nPlease check the server logs for more details.";
            }
        }

        private String mapTechnologyToTemplate(String technology) {
            Map<String, String> technologyMap = new HashMap<>();
            technologyMap.put("React with JS", "react-js");
            technologyMap.put("React with TS", "react-ts");
            technologyMap.put("Node.js MVC", "nodejs-mvc");
            technologyMap.put("SCSS templates", "scss");
            technologyMap.put("HTML/CSS projects", "html-css");

            String templateName = technologyMap.get(technology);
            if (templateName == null) {
                throw new IllegalArgumentException("Unsupported technology: " + technology);
            }

            return Paths.get(TEMPLATES_DIR, templateName).toString();
        }

        private void copyTemplateFiles(Path templateDir, Path projectDir) throws IOException {
            Files.walkFileTree(templateDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path targetDir = projectDir.resolve(templateDir.relativize(dir));
                    Files.createDirectories(targetDir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetFile = projectDir.resolve(templateDir.relativize(file));
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
            });
        }

        private void generateReadme(Path projectDir, String technology, String projectName) throws IOException {
            Path readmePath = projectDir.resolve("README.md");
            String readmeContent = "# " + projectName + "\n\n" +
                                   "This project was generated using the Project Generator for " + technology + ".\n\n" +
                                   "## How to run\n\n" +
                                   "1. Navigate to the project directory\n" +
                                   "2. Install dependencies (if applicable)\n" +
                                   "3. Start the development server\n\n" +
                                   "For more detailed instructions, please refer to the documentation of " + technology + ".";
            Files.write(readmePath, readmeContent.getBytes());
        }
    }
}