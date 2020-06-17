package com.crowd.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.crowd.entity.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.crowd.api.MySQLRemoteService;
import com.crowd.config.OSSProperties;
import com.crowd.constant.CrowdConstant;
import com.crowd.utils.CrowdUtils;
import com.crowd.utils.ResultEntity;

@Controller
public class ProjectConsumerController {

    @Autowired
    private OSSProperties ossProperties;
    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    /**
     * 接收表单数据
     *
     * @param projectVO         接收除了上传图片之外的其他普通数据
     * @param headerPicture     接收上传的头图
     * @param detailPictureList 接收上传的详情图片
     * @param session           用来将收集了一部分数据的 ProjectVO 对象存入 Session 域
     * @param modelMap          用来在当前操作失败后返回上一个表单页面时携带提示消息
     * @return 以完整的访问路径前往下一个收集回报信息的页面
     * @throws IOException IOException
     */
    @RequestMapping("/create/project/information")
    public String saveProjectBasicInfo(ProjectVO projectVO, MultipartFile headerPicture,
                                       List<MultipartFile> detailPictureList, HttpSession session, ModelMap modelMap) throws IOException {
        // 完成头图上传
        // 获取当前 headerPicture 对象是否为空
        boolean headerPictureIsEmpty = headerPicture.isEmpty();
        if (headerPictureIsEmpty) {
            // 如果没有上传头图则返回到表单页面并显示错误消息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_EMPTY);
            return "project-launch";
        }
        // 如果用户确实上传了有内容的文件，则执行上传
        ResultEntity<String> uploadHeaderPicResultEntity = CrowdUtils.uploadFileToOss(ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret(), headerPicture.getInputStream(),
                ossProperties.getBucketName(), ossProperties.getBucketDomain(), headerPicture.getOriginalFilename());
        String result = uploadHeaderPicResultEntity.getResult();
        // 判断头图是否上传成功
        if (ResultEntity.SUCCESS.equals(result)) {
            // 如果成功则从返回的数据中获取图片访问路径
            String headerPicturePath = uploadHeaderPicResultEntity.getData();
            // 存入 ProjectVO 对象中
            projectVO.setHeaderPicturePath(headerPicturePath);
        } else {
            // 如果上传失败则返回到表单页面并显示错误消息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);
            return "project-launch";
        }

        // 上传详情图片
        // 创建一个用来存放详情图片路径的集合
        List<String> detailPicturePathList = new ArrayList<>();
        // 检查 detailPictureList 是否有效
        if (detailPictureList == null || detailPictureList.size() == 0) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
            return "project-launch";
        }
        // 遍历 detailPictureList 集合
        for (MultipartFile detailPicture : detailPictureList) {
            // 当前 detailPicture 是否为空
            if (detailPicture.isEmpty()) {
                // 检测到详情图片中单个文件为空也是回去显示错误消息
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
                return "project-launch";
            }
            // 执行上传
            ResultEntity<String> detailUploadResultEntity = CrowdUtils.uploadFileToOss(ossProperties.getEndPoint(),
                    ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret(), detailPicture.getInputStream(),
                    ossProperties.getBucketName(), ossProperties.getBucketDomain(),
                    detailPicture.getOriginalFilename());
            // 检查上传结果
            String detailUploadResult = detailUploadResultEntity.getResult();
            if (ResultEntity.SUCCESS.equals(detailUploadResult)) {
                String detailPicturePath = detailUploadResultEntity.getData();
                // 收集刚刚上传的图片的访问路径
                detailPicturePathList.add(detailPicturePath);
            } else {
                // 如果上传失败则返回到表单页面并显示错误消息
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_UPLOAD_FAILED);
                return "project-launch";
            }
        }
        // 将存放了详情图片访问路径的集合存入ProjectVO 中
        projectVO.setDetailPicturePathList(detailPicturePathList);

        // 后续操作
        // 将ProjectVO 对象存入Session 域
        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);
        // 以完整的访问路径前往下一个收集回报信息的页面
        return "redirect:http://www.crowd.com/project/return/info/page";
    }

    // JavaScript代码：formData.append("returnPicture", file);
    // returnPicture是请求参数的名字
    // file是请求参数的值，也就是要上传的文件

    /**
     * 接收页面异步上传的图片
     *
     * @param returnPicture 接收用户上传的文件
     * @return 返回上传的结果
     * @throws IOException IOException
     */
    @ResponseBody
    @RequestMapping("/create/upload/return/picture.json")
    public ResultEntity<String> uploadReturnPicture(@RequestParam("returnPicture") MultipartFile returnPicture)
            throws IOException {
        // 执行文件上传
        ResultEntity<String> uploadReturnPicResultEntity = CrowdUtils.uploadFileToOss(ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret(), returnPicture.getInputStream(),
                ossProperties.getBucketName(), ossProperties.getBucketDomain(), returnPicture.getOriginalFilename());
        // 返回上传的结果
        return uploadReturnPicResultEntity;
    }

    /**
     * 接收整个回报信息数据
     *
     * @param returnVO 表单属性
     * @param session  Session域
     * @return 返回操作结果
     */
    @ResponseBody
    @RequestMapping("/create/save/return.json")
    public ResultEntity<String> saveReturn(ReturnVO returnVO, HttpSession session) {
        try {
            // 从 session 域中读取之前缓存的 ProjectVO 对象
            ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
            // 判断 projectVO 是否为 null
            if (projectVO == null) {
                return ResultEntity.failed(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
            }
            // 从 projectVO 对象中获取存储回报信息的集合
            List<ReturnVO> returnVOList = projectVO.getReturnVOList();

            // 判断 returnVOList 集合是否有效
            if (returnVOList == null || returnVOList.size() == 0) {
                // 创建集合对象对 returnVOList 进行初始化
                returnVOList = new ArrayList<>();
                // 为了让以后能够正常使用这个集合，设置到 projectVO 对象中
                projectVO.setReturnVOList(returnVOList);
            }
            // 将收集了表单数据的 returnVO 对象存入集合
            returnVOList.add(returnVO);
            // 把数据有变化的 ProjectVO 对象重新存入 Session 域
            // 以确保新的数据最终能够存入 Redis
            session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);
            // 所有操作成功完成返回成功
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/create/confirm")
    public String saveConfirm(ModelMap modelMap, HttpSession session, MemberConfirmInfoVO memberConfirmInfoVO) {

        // 从 Session域读取之前临时存储的 ProjectVO 对象
        ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

        // 如果 projectVO 为  null
        if (projectVO == null) {
            throw new RuntimeException(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
        }

        // 将确认信息数据设置到 projectVO 对象中
        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);

        // 从 Session 域读取当前登录的用户
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_MEMBER_NAME);

        Integer memberId = memberLoginVO.getId();

        // 调用远程方法保存 projectVO对象
        ResultEntity<String> saveResultEntity = mySQLRemoteService.saveProjectVORemote(projectVO, memberId);

        // 判断远程的保存操作是否成功
        String result = saveResultEntity.getResult();
        if (ResultEntity.FAILED.equals(result)) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveResultEntity.getMessage());

            return "project-confirm";
        }

        // 将临时的 ProjectVO 对象从 Session 域移除
        session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

        // 如果远程保存成功则跳转到最终完成页面
        return "redirect:http://www.crowd.com/project/create/success";
    }

    @RequestMapping("/get/project/detail/{projectId}")
    public String getProjectDetail(@PathVariable("projectId") Integer projectId, Model model) {
        ResultEntity<DetailProjectVO> resultEntity =
                mySQLRemoteService.getDetailProjectVORemote(projectId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            DetailProjectVO detailProjectVO = resultEntity.getData();
            model.addAttribute("detailProjectVO", detailProjectVO);
        }
        return "project-show-detail";
    }
}
