/**
 * MIT License
 *
 * MSUSEL Design Pattern Generator
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
#include "process.h"
#include <iostream>
#include <string>
#include <glob.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>

#ifndef JARFILE
#error No JARFILE defined!
#endif

using namespace std;

#ifdef EMBEDDED_JAR_FILE
// see http://stackoverflow.com/questions/6785214/how-to-embed-a-file-into-an-executable-file and http://gareus.org/wiki/embedding_resources_in_executables
extern const unsigned char _binary_embedded_jar_file_start[];
extern const unsigned char _binary_embedded_jar_file_end[];
#endif

int runJava(string &javaBin, string &jarfile) {
  char * const args[] = { (char *)javaBin.c_str(),
                          (char *)"-jar",
                          (char *)jarfile.c_str(),
                          NULL };
#ifndef QUIET
  cerr << "Trying " << javaBin << endl;
#endif
  Process p(args[0], args, false);
  return p.wait();
}

bool fileExists(string &file) {
  struct stat buf;
  if (stat(file.c_str(), &buf) == -1) {
    return false;
  }
  return true;
}

char *tmpjar = NULL;

static pid_t parentpid = getpid();
void cleanup(void) {
  if (getpid() != parentpid) {
    return;
  }
  if (tmpjar != NULL) {
    cerr << "deleting " << tmpjar << endl;
    unlink(tmpjar);
  }
}

int main(int argc, char *argv[])
{
#ifdef EMBEDDED_JAR_FILE
  tmpjar = strdup("/tmp/tmpjarfileXXXXXX");
  int fd = mkstemp(tmpjar);
  size_t len = _binary_embedded_jar_file_end - _binary_embedded_jar_file_start;
  cerr << "creating " << tmpjar << " with " << len << " bytes" << endl;
  write(fd, _binary_embedded_jar_file_start, len);
  fsync(fd);
  atexit(cleanup);

  string jarfile = tmpjar;
#else
  string jarfile = JARFILE;
#endif

  int numJREs = 0;

  // if the environment variable JAVA_HOME is set, we try this jvm 
  char *java_home = getenv("JAVA_HOME");
  if (java_home != NULL)
  {
    string javaBin = java_home;
    javaBin += "/bin/java";
    if (fileExists(javaBin)) {
      ++numJREs;
      if (runJava(javaBin, jarfile) == 0) {
        return 0;
      }
    }
  }

  // then, we'll try the PATH
  char *paths = getenv("PATH");
  if (paths != NULL) {
    paths = strdup(paths);
    char *path = strtok(paths, ":");
    while (path != NULL) {
      string javaBin = path;
      javaBin += "java";
      if (fileExists(javaBin)) {
        ++numJREs;
        if (runJava(javaBin, jarfile) == 0) {
          return 0;
        }
      }
      path = strtok(NULL, ":");
    }
    free(paths);
  }


  // try any jvm in /usr/lib/jvm/*/bin/java
  glob_t globbuf = { 0, NULL, 0};
  if (glob("/usr/lib/jvm/*/bin/java", 0, NULL, &globbuf) == 0) {
    for (int i=0; i<globbuf.gl_pathc; ++i) {
      string javaBin = globbuf.gl_pathv[i];
      if (fileExists(javaBin)) {
        ++numJREs;
        if (runJava(javaBin, jarfile) == 0) {
          return 0;
        }
      }
    }
  }
  globfree(&globbuf);

  // see if we can find the jvm via update-alternatives
  char *args[] = { (char *)"/usr/bin/update-alternatives",
                   (char *)"--list",
                   (char *)"java",
                   NULL };
  Process p(args[0], args);
  string javaBin;
  while (p.getline(javaBin) > 0) {
    if (fileExists(javaBin)) {
      ++numJREs;
      if (runJava(javaBin, jarfile) == 0) {
        return 0;
      }
    }
  }

  char *errmsg = (char *)"Could not launch "JARFILE;

  if (numJREs == 0)
  {
    errmsg = (char *)"Java is not installed!";
  }

  cerr << errmsg << endl;

  if (getenv("DISPLAY") != NULL)
  {
    char *args[] = { (char *)"/usr/bin/xmessage",
                     (char *)"-center",
                     errmsg,
                   NULL };
    Process p(args[0], args);
    p.wait();
  }

  return 1;
}
