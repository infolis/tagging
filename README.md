The tagging project tags research data, in our case studies, with concepts of a thesaurus.

These concepts can either be determined by exploiting links to publications or by tagging the abstract of the study. 
In out examples, we use the thesaurus TheSoz, the studies from da|ra and the publications from SSOAR.

Usage:
- download FastJoin (http://dbgroup.cs.tsinghua.edu.cn/wangjn/projects/fastjoin/)
- adapt the infolis.properties file according to you needs, take especially care of the FastJoin path and the data paths
- run the Tagging.jar with the command "java -jar Tagging.jar CONFIG TYPE OUT"
- CONFIG is the file to your properties file (infolis.properties)
- TYPE can either be Link or Abstract where Link uses the link files to tagg research data and abstract uses the abstracts
- OUT points to the file where the output should be written to. The format will be: STUDYID\tCONCEPTID\tScore where score represents how certain it is that the assigned concept holds

## Running current code with gradle

* Install [gradle](http://gradle.org)
* Run `gradle run OPTIONS` where Options are
  * `-DconfigFile="CONFIG"` (seea CONFIG above)
  * `-Dtype="(Link|Abstract)"` (see TYPE above)
  * `-DoutFile="OUT"` (see OUT above)

e.g.

```
gradle run -DconfigFile="infolis.properties" -Dtype="Link" -DoutFile="test.out"
```


