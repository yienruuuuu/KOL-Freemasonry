package io.github.yienruuuuu.bean.dto;

import lombok.*;

/**
 * 爬蟲取得的Twitter帳號資料
 *
 * @author Eric.Lee
 * Date: 2025/4/30
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class TwitterAccountDto {
    String accountName;
    String account;
    String followersCount;
    boolean isPremium;

    @Override
    public String toString() {
        return "TwitterAccountDto{" +
                "accountName='" + accountName + '\'' +
                ", account='" + account + '\'' +
                ", followersCount='" + followersCount + '\'' +
                ", isPremium=" + isPremium +
                '}';
    }
}
