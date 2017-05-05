package io.gmas.fuze.components;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.gmas.fuze.R;
import io.gmas.fuze.commons.BaseActivity;
import io.gmas.fuze.commons.qualifiers.RequiresActivityViewModel;

import static io.gmas.fuze.commons.rx.transformers.Transformers.observeForUI;

@RequiresActivityViewModel(MainViewModel.class)
public class MainActivity extends BaseActivity<MainViewModel> {

    protected @BindView(R.id.bt_connect) Button connectButton;
    protected @BindView(R.id.email_text) AppCompatEditText email;
    protected @BindView(R.id.password_text) AppCompatEditText password;

    @OnTextChanged(R.id.email_text) void onEmailTextChanged(final @NonNull CharSequence email) {
        viewModel.inputs.email(email.toString());
    }

    @OnTextChanged(R.id.password_text) void onPasswordTextChanged(final @NonNull CharSequence password) {
        viewModel.inputs.password(password.toString());
    }

    @OnClick(R.id.bt_connect) void onClickConnect() {
        viewModel.inputs.onClickConnect();
    }

    @Override protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        ButterKnife.bind(this);

        viewModel.outputs.setLoginButtonIsEnabled()
                .compose(bindToLifecycle())
                .compose(observeForUI())
                .subscribe(this::setLoginButtonEnabled);

        viewModel.outputs.success()
                .compose(bindToLifecycle())
                .compose(observeForUI())
                .subscribe(__ -> this.success());
    }

    private void setLoginButtonEnabled(final boolean enabled) {
        connectButton.setEnabled(enabled);
    }

    private void success() {
        Toast.makeText(this, "Log in success", Toast.LENGTH_SHORT).show();
        email.setText("");
        password.setText("");
    }

}
