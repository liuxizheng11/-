package app.phj.com.testlib;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.deviceplatform.app.scene.SceneType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 项目名称：TestLib
 * <p>
 * 作者：phj
 * 日期：2017/7/24 下午3:27
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class SceneTestChooseTypeFragment extends RCBaseFragment {

    @BindView(R.id.button_slp)
    Button buttonSlp;
    @BindView(R.id.button_swp)
    Button buttonSwp;
    @BindView(R.id.button_snqx)
    Button buttonSnqx;
    @BindView(R.id.button_swqx)
    Button buttonSwqx;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_scene_choose_type, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.button_slp, R.id.button_swp, R.id.button_snqx, R.id.button_swqx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_slp:
                SceneTestActivity.goActivityChooseDevice(mActivity, SceneType.RUN);
                break;
            case R.id.button_swp:
                SceneTestActivity.goActivityChooseDevice(mActivity, SceneType.RUNGPS);
                break;
            case R.id.button_snqx:
                SceneTestActivity.goActivityChooseDevice(mActivity, SceneType.CYCLING);
                break;
            case R.id.button_swqx:
                SceneTestActivity.goActivityChooseDevice(mActivity, SceneType.CYCLINGGPS);
                break;
        }
    }
}
