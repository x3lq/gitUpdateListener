import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.BranchTrackingStatus;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if(args.length != 2 || args.length !=3 ) {
            System.out.println("Param repPath and branchToObserve needed");
        }

        String path = args[0];
        File repoFile = new File(path);

        String branchToWatch = args[1];

        String scriptToExecute = null;
        if(args.length == 3) {
            scriptToExecute = args[2];
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
}
