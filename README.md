# Artifact Appendix

Paper title: **tigro: Trust Infrastructure for Grassroots Organizing via Grounded Digital Annotations**

Requested Badge(s): **Available**

## Description

This repository contains the associated artifact for the paper "tigro: Trust Infrastructure for Grassroots Organizing via Grounded Digital Annotations".

It contains the reference implementations of Tigro and the benchmarks for the evaluation. The code is organized as follows:

* `app` contains the Android prototype application
* `library` contains the Tigro client-side library with the Tor experiments
* `exh` contains the EXH experiments

### Security/Privacy Issues and Ethical Concerns

This is a research prototype with cryptographic components that have not been audited by a third-party. You should not use this in a production setting. We do not intend to release the tigro protocol as a mainstream software application for use outside of academic settings until resources are available for sufficient penetration testing and maintenance. When it is ready for a full release, we intend to develop educational materials and work closely with organizations to configure tigro to their needs, and continue to improve the accessibility and usability of the tigro application.

Our experiments involved running traffic through the Tor network. While our client set sizes were small, we wanted to further minimize the load our evaluation imposed on the Tor network. To do so, we avoided concurrent executions which would require clients to open many Tor circuits in parallel, and also staggered our evaluations across multiple days. This is consistent with Tor community standards and specifications, for instance the Circuits specification section, which notes that "excessive circuit creation can impact the entire path of that circuit". The C Tor implementation additionally rate-limits circuit creation per client IP address. In the future, we hope to work with folks at the Tor Project to better integrate Tor into the tigro protocol in a way that serves activists in the context of the broader Tor user base.

## Environment

Please see the local `README.md` files in each folder for specific setup and explanation instructions.

### Accessibility

We will update this section with a link to the camera-ready version of the GitHub repository at the time of artifact acceptance.
