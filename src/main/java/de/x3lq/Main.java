package de.x3lq;

import de.x3lq.Notifier.EmailConfig;
import de.x3lq.Notifier.Notifier;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.BranchTrackingStatus;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {

	private static Properties config;

    public static void main(String[] args) {

		config = ConfigReader.readConfig();

		if(config == null) {
			return;
		}

        String path = config.getProperty("repositoryPath");
        File repoFile = new File(path);

        String branchToWatch = config.getProperty("branchName");

        String scriptToExecute = config.getProperty("scriptPath");
        if( !new File(scriptToExecute).exists() ) {
        	scriptToExecute = null;
		}

        if(repoFile.exists()) {

            try {
                Git git = Git.open(repoFile);
                Repository repository = git.getRepository();
                git.fetch().call();

                for(Ref ref : git.branchList().call()) {
                    if (ref.getName().contains(branchToWatch)) {
                        List<Integer> counts = getCounts(repository, ref.getName());
                        System.out.println("For branch: " + ref.getName());
                        System.out.println("Commits behind : " + counts.get(1));

                        if(counts.get(1) > 0) {
                            System.out.println("Performing update");
                            git.checkout().setName(branchToWatch).call();
                            git.pull().call();

                            if(scriptToExecute != null) {
                                String[] cmdScript = new String[]{"/bin/bash", scriptToExecute};
                                System.out.println("Update done. \nExecuting Script");
                                Process process = new ProcessBuilder(cmdScript).start();

                                process.waitFor();
                            }

							Notifier.notifyAllEmail(createEmailConfig());

                            System.out.println("Done");
                        }
                    }
                }
            } catch (Exception e) {
                if(e instanceof RefNotFoundException) {
                    System.out.println("no such branch " + branchToWatch );
                }

                e.printStackTrace();
            }
        }
    }

    private static List<Integer> getCounts(org.eclipse.jgit.lib.Repository repository, String branchName) throws IOException {
        BranchTrackingStatus trackingStatus = BranchTrackingStatus.of(repository, branchName);
        List<Integer> counts = new ArrayList();
        if (trackingStatus != null) {
            counts.add(trackingStatus.getAheadCount());
            counts.add(trackingStatus.getBehindCount());
        } else {
            System.out.println("Returned null, likely no remote tracking of branch " + branchName);
            counts.add(0);
            counts.add(0);
        }
        return counts;
    }

    private static EmailConfig createEmailConfig() {
    	boolean auth = Boolean.valueOf(config.getProperty("mail.smtp.auth"));
    	boolean tls = Boolean.valueOf(config.getProperty("mail.smtp.tls.enable"));
    	String host = config.getProperty("mail.smtp.host");
    	int port = Integer.parseInt(config.getProperty("mail.smtp.port"));
    	String user = config.getProperty("mail.user");
    	String pw = config.getProperty("mail.pw");
    	String text = config.getProperty("text");

    	String[] mails = config.getProperty("recipiants").split(", ");

    	return new EmailConfig(auth, tls, host, port, user, pw, text, mails);
	}
}
