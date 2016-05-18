/* Copyright 2015 Samsung Electronics Co., LTD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gearvrf;

import java.util.List;

import org.gearvrf.utility.Exceptions;

/**
 * Base class for defining interfaces to extend the scene object.
 *
 * A GVRSceneObject can have any number of interfaces but only
 * one instance of each class. 
 * 
 * {@link GVRSceneObject.addInterface }
 */
public class GVRComponent extends GVRHybridObject {
    // private static final String TAG = Log.tag(GVRComponent.class);

    /**
     * Normal constructor
     *
     * @param gvrContext
     *            The current GVRF context
     * @param nativePointer
     *            The native pointer, returned by the native constructor
     */
    protected GVRComponent(GVRContext gvrContext, long nativeConstructor, Class<? extends GVRComponent> type) {
        super(gvrContext, nativeConstructor);
        long nativeType = GVRComponent.typeFromClass(type);
        NativeComponent.setType(getNative(), nativeType);
        isEnabled = true;
    }

    public GVRComponent(GVRContext gvrContext, long nativeConstructor, Class<? extends GVRComponent> type, GVRSceneObject owner) {
        super(gvrContext, nativeConstructor);
        long nativeType = GVRComponent.typeFromClass(type);
        NativeComponent.setType(getNative(), nativeType);
        setOwnerObject(owner);
        isEnabled = true;
    }
    
    /**
     * Special constructor, for descendants like {#link GVRMeshEyePointee} that
     * need to 'unregister' instances.
     * 
     * @param gvrContext
     *            The current GVRF context
     * @param nativePointer
     *            The native pointer, returned by the native constructor
     * @param cleanupHandlers
     *            Cleanup handler(s).
     * 
     *            <p>
     *            Normally, this will be a {@code private static} class
     *            constant, so that there is only one {@code List} per class.
     *            Descendants that supply a {@code List} and <em>also</em> have
     *            descendants that supply a {@code List} should use
     *            {@link CleanupHandlerListManager} to maintain a
     *            {@code Map<List<NativeCleanupHandler>, List<NativeCleanupHandler>>}
     *            whose keys are descendant lists and whose values are unique
     *            concatenated lists - see {@link GVREyePointeeHolder} for an
     *            example.
     */
    protected GVRComponent(GVRContext gvrContext, long nativePointer, Class<? extends GVRComponent> type,
            List<NativeCleanupHandler> cleanupHandlers) {
        super(gvrContext, nativePointer, cleanupHandlers);
        long nativeType = GVRComponent.typeFromClass(type);
        NativeComponent.setType(getNative(), nativeType);
    }

    protected GVRSceneObject owner;
    protected boolean isEnabled;

    /**
     * @return The {@link GVRSceneObject} this object is currently attached to.
     */
    public GVRSceneObject getOwnerObject() {
        if (owner != null) {
            return owner;
        }

        throw Exceptions.RuntimeAssertion("No Java owner: %s", getClass()
                .getSimpleName());
    }

    public void setOwnerObject(GVRSceneObject owner) {
        this.owner = owner;
    }

    /**
     * Checks if the {@link GVRComponent} is attached to a {@link GVRSceneObject}.
     *
     * @return true if a {@link GVRSceneObject} is attached.
     */
    public boolean hasOwnerObject() {
        return owner != null;
    }
    /**
     * Enable the component so it will be active in the scene.
     */
    public void enable() {
        isEnabled = true;
    }

    /**
     * Disable the component so it will not be active in the scene.
     */
    public void disable() {
        isEnabled = false;
    }
    
    /**
     * Get the enable/disable status for the component.
     * 
     * @return true if interface is enabled, false if component is disabled.
     */
    public boolean isEnabled() {
        return isEnabled;
    }
    
    public long getType() {
        return NativeComponent.getType(getNative());
    }
    
    /**
     * Get the transform of the scene object this component is attached to.
     * 
     * @return GVRTransform of scene object
     */
    public GVRTransform getTransform() {
        return getOwnerObject().getTransform();
    }
    
    /**
     * Get the component of the specified class attached to the scene object.
     * 
     * If the scene object that owns this component also has an interface
     * of the given class, it will be returned.
     * @return GVRComponent of specified class or null if none exists.
     */
    public GVRComponent getComponent(Class<? extends GVRComponent> componentClass) {
        return getOwnerObject().getComponent(componentClass);
    }
    
    static public long typeFromClass(Class<? extends GVRComponent> jclass) {
        String s = jclass.getSimpleName();
        long h = 98764321261L; 
        int l = s.length();
        char[] chars = s.toCharArray();
        
        for (int i = 0; i < l; i++) {
          h = 31 * h + chars[i];
        }
        return h;
    }

}

class NativeComponent {
    static native long getType(long component);
    static native void setType(long component, long type);
}
