#!/bin/sh

echo "----- Run this from the classpath/java/util/zip directory -----"
echo "----- Run it with 'juz' as an argument to put *precompiled*"
echo "----- classes into a jazzlib-0.0v-juz.tar.gz package"
echo "----- "
echo "----- Edit this script to change the release number       -----"
echo "----- Do rm -rf dist when you're finished                 -----"
echo "----- 23 October 2001 John Leuner <jewel@debian.org>      -----"

RELEASE_NUMBER=03


case "$1" in
    juz)
    mkdir -p dist/java/util/zip

    #make source archive
    cp *.java dist/java/util/zip
    pushd dist
    
    rm -rf javadoc
    mkdir javadoc
    javadoc -sourcepath . -d javadoc/ java.util.zip
    cp ../../../../COPYING.LIB .

    md5sum java/util/zip/*.java > md5sums
    gpg --clearsign md5sums
    tar cvfz jazzlib-0.$RELEASE_NUMBER-juz.tar.gz java/util/zip/*.java javadoc md5sums md5sums.asc COPYING.LIB
    rm -f md5sums
    rm -f md5sums.asc
    
    popd

    #make binary distro second
    cp *.class dist/java/util/zip
    pushd dist
    
    md5sum java/util/zip/*.class > md5sums
    gpg --clearsign md5sums
    tar cvfz jazzlib-binary-0.$RELEASE_NUMBER-juz.tar.gz java/util/zip/*.class javadoc md5sums md5sums.asc COPYING.LIB
    rm -f md5sums
    rm -f md5sums.asc
    
    popd
    ;;
    *)
    #copy files to dist directory and make net.sf.jazzlib the package name
    
    mkdir -p dist/net/sf/jazzlib
    cp *.java dist/net/sf/jazzlib
    for i in dist/net/sf/jazzlib/*.java ; do 
	sed -e "s/java\.util\.zip/net.sf.jazzlib/" < $i > $i.tmp ;
	mv $i.tmp $i;
    done
	
    pushd dist
	
    #make the javadoc
    rm -rf javadoc
    mkdir javadoc
    javadoc -sourcepath . -d javadoc/ net.sf.jazzlib

    #make the source dist
    md5sum net/sf/jazzlib/*.java > md5sums
    gpg --clearsign md5sums
    cp ../../../../COPYING.LIB .
    tar cvfz jazzlib-0.$RELEASE_NUMBER.tar.gz net/sf/jazzlib/*.java javadoc md5sums md5sums.asc COPYING.LIB
    rm -f md5sums
    rm -f md5sums.asc

    #compile the source
    javac net/sf/jazzlib/*.java

    #make the binary distribution
    md5sum net/sf/jazzlib/*.class > md5sums
    gpg --clearsign md5sums
    tar cvfz jazzlib-binary-0.$RELEASE_NUMBER.tar.gz net/sf/jazzlib/*.class javadoc md5sums md5sums.asc COPYING.LIB
    rm -f md5sums
    rm -f md5sums.asc

    #back to dir
    popd

    ;;
esac
ls -la dist/*.tar.gz



