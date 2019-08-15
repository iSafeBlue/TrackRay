package com.trackray.module.inner.codeAudit;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/14 17:08
 */
public interface CodeVulnRepository extends CrudRepository<Vuln, Long> {
    public List<Vuln> findAllByName(String name);

}
