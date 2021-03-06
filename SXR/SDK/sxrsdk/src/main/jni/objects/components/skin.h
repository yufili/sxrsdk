#ifndef SKIN_H_
#define SKIN_H_

#include <vector>
#include "glm/glm.hpp"
#include "glm/gtc/matrix_transform.hpp"
#include "objects/components/component.h"
#include "objects/components/skeleton.h"

namespace sxr {
class Renderer;
class Shader;
class Transform;
class UniformBlock;

class Skin : public Component
{
public:
    Skin(Skeleton& skel) ;
    virtual ~Skin();

    static long long getComponentType()
    {
        return COMPONENT_TYPE_SKIN;
    }

    int getNumBones() const { return mBoneMap.size(); }

    void setBoneMap(const int* bonemap, int numBones);
    void setSkeleton(Skeleton* skel);
    void getInverseBindPose(float* inverseBindPose, int numBones);
    void setInverseBindPose(const float* inverseBindPose, int numBones);
    void scalePositions(float sf);
    void bindBuffer(Renderer* renderer, Shader* shader);
    bool updateGPU(Renderer* renderer, Shader* shader);
    UniformBlock* getUniformBlock() { return mBonesBuffer; }

private:
    Skin(const Skin& sksel) = delete;
    Skin(Skin&& s) = delete;
    Skin& operator=(const Skin& s) = delete;
    Skin& operator=(Skin&& s) = delete;
    Node* findCommonParent(Node* node1, Node* node2) const;

private:
    std::mutex  mLock;
    Skeleton* mSkeleton;
    std::vector<int> mBoneMap;
    glm::mat4* mInverseBindPose;
    UniformBlock* mBonesBuffer;
};

}
#endif
