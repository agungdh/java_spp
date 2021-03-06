/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.test.Models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

/**
 *
 * @author user
 */
@Table("spp")
@BelongsToParents({ 
    @BelongsTo(parent = KelasModel.class, foreignKeyName = "id_kelas"),
    @BelongsTo(parent = TahunAjaranModel.class, foreignKeyName = "id_tahun_ajaran")
}) 
public class SppModel extends Model {}