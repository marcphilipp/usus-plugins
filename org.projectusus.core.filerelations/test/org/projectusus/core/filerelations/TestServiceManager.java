package org.projectusus.core.filerelations;

import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;

public class TestServiceManager {

	public static <T> Set<T> asSet(T... items) {
		return new HashSet<T>(Arrays.asList(items));
	}

	public static IFile createFileMock() {
		return mock(IFile.class);
	}

	public static ClassDescriptor createClassDescriptorMock() {
		return mock(ClassDescriptor.class);
	}

	public static ClassDescriptor createDescriptor(IFile file, Classname clazz) {
		return new ClassDescriptor(file, clazz, new Packagename("packagename1"));
	}

	public static ClassDescriptor createDescriptor(IFile file) {
		return createDescriptor(file, new Classname("classname1"));
	}
	
}