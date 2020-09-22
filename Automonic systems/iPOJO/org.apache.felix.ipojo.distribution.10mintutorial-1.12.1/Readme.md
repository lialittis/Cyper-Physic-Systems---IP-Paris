# A simple example

In this tutorial we will present how to:

* Publish an OSGi service
* Require an OSGi service
* Use lifecycle callbacks to activate and deactivate components

## Presentation of the Spell application

To illustrate iPOJO features, we will implement a very siple application. This application is composed by three components: **A component implementing a dictionary service**, **a component requiring the dictionary service and providing a spellchecker service**, **a component requiring the spellchecker and providing an user interface**.

## Preparing the tutorial

This tutorial is based on Ant. So, you need to have the Ant program accessible in your path (see [here](http://ant.apache.org/) to download and install Ant). 

The archive we are going to use contains seven directories:

* spell.services contains service interfaces used by the applications
* spell.english contains an implementation of the Dictionary service (containing English words)
* spell.checker contains an implementation of a Spell Checker. The spell checker requires a dictionary service and check if an input passage is correct (according to the words contained in the dictionary).
* spell.gui contains a very simple user interface. This component uses a spell checker service. Then the user can interact with the spell checker with this user interface.
* The task directory contains Ant tasks used to build the project
* The solution directory contains an already developed version of the application.
* Finally, the felix folder contains a configured version of the Felix runtime.

