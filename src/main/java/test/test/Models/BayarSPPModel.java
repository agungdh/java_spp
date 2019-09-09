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
@Table("bayar_spp")
@BelongsToParents({ 
    @BelongsTo(parent = SppModel.class, foreignKeyName = "id_spp"),
    @BelongsTo(parent = SiswaModel.class, foreignKeyName = "id_siswa")
}) 
public class BayarSPPModel extends Model {}